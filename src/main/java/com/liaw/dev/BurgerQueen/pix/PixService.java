package com.liaw.dev.BurgerQueen.pix;

import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import com.liaw.dev.BurgerQueen.dto.dtos.PaymentDTO;
import com.liaw.dev.BurgerQueen.dto.request.PixRequest;
import com.liaw.dev.BurgerQueen.entity.Order;
import com.liaw.dev.BurgerQueen.entity.Payment;
import com.liaw.dev.BurgerQueen.enums.OrderStatus;
import com.liaw.dev.BurgerQueen.enums.PaymentStatus;
import com.liaw.dev.BurgerQueen.exception.exceptions.OrderNotFoundException;
import com.liaw.dev.BurgerQueen.exception.exceptions.PaymentNotFoundException;
import com.liaw.dev.BurgerQueen.mapper.PaymentMapper;
import com.liaw.dev.BurgerQueen.repository.OrderRepository;
import com.liaw.dev.BurgerQueen.repository.PaymentRepository;
import com.liaw.dev.BurgerQueen.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PixService {

    private final PaymentMapper mapper;
    private final EmailService emailService;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public JSONObject createPixCharge(PixRequest request){
        JSONObject options = configuration();
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(()-> new OrderNotFoundException("Pedido não encontrado."));
        JSONObject body = new JSONObject();
        body.put("calendario", new JSONObject().put("expiracao", 600));
        body.put("devedor", new JSONObject()
                .put("nome", order.getUser().getName()).put("cpf", order.getUser().getCpf()));
        body.put("valor", new JSONObject()
                .put("original", order.getAmount().setScale(2, RoundingMode.HALF_UP).toString()));
        body.put("chave", "lucasliaw50@gmail.com");
        body.put("solicitacaoPagador", "Pagamento do pedido #" + order.getId());

        try {
            EfiPay efiPay = new EfiPay(options);
            JSONObject response = efiPay.call("pixCreateImmediateCharge", new HashMap<String, String>(), body);
            return response;
        }catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }

    public PaymentDTO createPayment(PixRequest request){
        Payment payment = new Payment();
        JSONObject pixCharge = createPixCharge(request);
        String txid = pixCharge.getString("txid");
        String locationCode = extractLocationCode(pixCharge);
        String paymentLink = buildPaymentLink(locationCode);
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(()-> new OrderNotFoundException("Pedido não encontrado."));
        payment.setStatus(PaymentStatus.PENDING);
        payment.setOrder(order);
        payment.setTxid(txid);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setPaymentLink(paymentLink);
        paymentRepository.save(payment);
        order.setPayment(payment);
        orderRepository.save(order);
        return mapper.toDTO(payment);
    }

    private String checkPayment(String txid){
        JSONObject options = configuration();
        HashMap<String, String> params = new HashMap<>();
        params.put("txid", txid);
        try {
            EfiPay efiPay = new EfiPay(options);
            JSONObject response = efiPay.call("pixDetailCharge", params, new JSONObject());
            if (response.has("pix")){
                JSONArray pixArray = response.getJSONArray("pix");
                if (pixArray.length() > 0){
                    return "Concluida" ;
                }
            }
        }catch (EfiPayException e){
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getErrorDescription());
            System.out.println(e.getError());
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getLocalizedMessage());
        }
        return "Pendente";
    }

    private Boolean checkPaymentExpired(Payment payment){
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(payment.getCreatedAt().plusMinutes(10));
    }

    public String processPayment(String txid){
        String status = checkPayment(txid);
        Payment payment = paymentRepository.findByTxid(txid)
                .orElseThrow(()-> new PaymentNotFoundException("Pagamento não encontrado."));
        if ("CONCLUIDA".equalsIgnoreCase(status)){
            if (payment.getStatus() == PaymentStatus.PENDING){
                payment.setEndedAt(LocalDateTime.now());
                payment.setStatus(PaymentStatus.COMPLETED);
                Order order = orderRepository.findByPaymentId(payment.getId());
                order.setStatus(OrderStatus.PREPARING);
                paymentRepository.save(payment);
                orderRepository.save(order);
                emailService.sendPaymentConfirmation(
                        order.getUser().getEmail(),
                        order.getUser().getName(),
                        order.getId(),
                        payment.getOrder().getAmount(),
                        payment.getId()
                );
                return "Pagamento concluído com sucesso.";
            }
        }
        return "Pagamento ainda pendente.";
    }

    public Boolean processPayments(String txid){
        String status = checkPayment(txid);
        return "CONCLUIDA".equalsIgnoreCase(status);
    }

    @Transactional
    @Scheduled(fixedRate = 90000)
    public void checkPaymentStatus(){
        System.out.println("---------------- Iniciando verificação de pagamentos pendentes ----------------");
        List<Payment> payments = paymentRepository.findByStatus(PaymentStatus.PENDING);
        payments.stream()
                .filter(payment -> checkPaymentExpired(payment))
                .forEach(payment-> {
                    payment.setStatus(PaymentStatus.CANCELLED);
                    payment.setEndedAt(LocalDateTime.now());
                    paymentRepository.save(payment);
                    System.out.println("Pagamento com TXID " + payment.getTxid() + " foi cancelado por expiração.");
                });
        List<Payment> pendingPayments = paymentRepository.findByStatus(PaymentStatus.PENDING);
        pendingPayments.stream()
                .filter(payment -> processPayments(payment.getTxid()))
                .forEach(payment -> {
                    payment.setEndedAt(LocalDateTime.now());
                    payment.setStatus(PaymentStatus.COMPLETED);
                    Order order = orderRepository.findByPaymentId(payment.getId());
                    order.setStatus(OrderStatus.PREPARING);
                    paymentRepository.save(payment);
                    orderRepository.save(order);
                    emailService.sendPaymentConfirmation(
                            order.getUser().getEmail(),
                            order.getUser().getName(),
                            order.getId(),
                            payment.getOrder().getAmount(),
                            payment.getId()
                    );
                    System.out.println("Pagamento com TXID " + payment.getTxid() + " foi concluído com sucesso.");
                });
    }

    private String extractLocationCode(JSONObject pixCharge) {
        try {
            String location = pixCharge.optString("location", "");
            if (!location.isEmpty()) {
                String[] parts = location.split("/");
                return parts[parts.length - 1];
            }
        } catch (Exception e) {
            return pixCharge.getString("txid");
        }
        return "Erro ao extrair o código da localização";
    }

    private String buildPaymentLink(String locationCode){
        return "https://pix.sejaefi.com.br/cob/pagar/" + locationCode;
    }


    private JSONObject configuration(){
        Credentials credentials = new Credentials();
        JSONObject options = new JSONObject();
        options.put("client_id", credentials.getClientId());
        options.put("client_secret", credentials.getClientSecret());
        options.put("certificate", credentials.getCertificate());
        options.put("sandbox", credentials.getSandbox());
        return options;
    }


}

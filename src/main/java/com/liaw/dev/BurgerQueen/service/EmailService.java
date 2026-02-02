package com.liaw.dev.BurgerQueen.service;

import com.liaw.dev.BurgerQueen.entity.User;
import com.liaw.dev.BurgerQueen.exception.exceptions.EmailFailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {


    private String emailFrom;
    private JavaMailSender mailSender;

    public EmailService(@Value("${spring.mail.username}") String emailFrom,
                        JavaMailSender mailSender) {
        this.emailFrom = emailFrom;
        this.mailSender = mailSender;
    }

    public void createUserEmail(User user){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailFrom);
            helper.setTo(user.getEmail());
            helper.setSubject("Ative sua conta no Burger Queen");

            String htmlContent = buildActivationEmailHtml(user.getName(), user.getCode());
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailFailException("Falha ao enviar email de ativação");
        }
    }

    private String buildActivationEmailHtml(String userName, String code) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f8f8f8; padding: 20px; }
                    .container { max-width: 600px; margin: 0 auto; background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
                    .header { background: linear-gradient(135deg, #FF8C00, #FF4500); padding: 30px; text-align: center; }
                    .header h1 { color: white; margin: 0; font-size: 28px; }
                    .content { padding: 40px; }
                    .code-box { background: #FFF5E6; border: 2px dashed #FF8C00; border-radius: 8px; padding: 20px; text-align: center; margin: 30px 0; font-size: 24px; font-weight: bold; color: #333; letter-spacing: 2px; }
                    .button { display: inline-block; background: #FF8C00; color: white; padding: 15px 30px; text-decoration: none; border-radius: 8px; font-weight: bold; font-size: 16px; margin-top: 20px; }
                    .footer { background: #f1f1f1; padding: 20px; text-align: center; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🍔 BurgerQueen</h1>
                    </div>
                    <div class="content">
                        <h2>Olá, %s!</h2>
                        <p>Estamos felizes em ter você no BurgerQueen! Para ativar sua conta e começar a pedir seus hambúrgueres favoritos, insira o código a baixo na verificação:</p>
                  
                        <div class="code-box">%s</div>
                        
                        <p>Este código expira em 24 horas.</p>
                        <p>Se você não criou uma conta no BurgerQueen, ignore este email.</p>
                    </div>
                    <div class="footer">
                        <p>© 2024 BurgerQueen. Todos os direitos reservados.</p>
                        <p>Este é um email automático, por favor não responda.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(userName, code, code);
    }

    public void sendPaymentConfirmation(String to, String userName, Long orderId,
                                        BigDecimal total, Long transactionId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(emailFrom);
            helper.setTo(to);
            helper.setSubject("✅ Pagamento Confirmado - Pedido #" + orderId);

            String htmlContent = buildPaymentConfirmationHtml(userName, orderId, total, transactionId);
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new EmailFailException("Erro ao enviar o email");
        }
    }

    private String buildPaymentConfirmationHtml(String userName, Long orderId,
                                                BigDecimal total, Long transactionId) {
        String template = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; background-color: #f8f8f8; padding: 20px; margin: 0; }\n" +
                "        .container { max-width: 600px; margin: 0 auto; background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }\n" +
                "        .header { background: #4CAF50; padding: 30px; text-align: center; }\n" +
                "        .header h1 { color: white; margin: 0; font-size: 28px; }\n" +
                "        .content { padding: 40px; }\n" +
                "        .info-box { background: #f9f9f9; border-left: 4px solid #4CAF50; padding: 20px; margin: 20px 0; }\n" +
                "        .info-box h3 { margin-top: 0; color: #333; }\n" +
                "        .status { background: #E8F5E9; color: #2E7D32; padding: 10px; border-radius: 6px; display: inline-block; font-weight: bold; }\n" +
                "        .footer { background: #f1f1f1; padding: 20px; text-align: center; color: #666; font-size: 12px; }\n" +
                "        .order-details { width: 100%%; border-collapse: collapse; margin: 20px 0; }\n" +
                "        .order-details td { padding: 12px; border-bottom: 1px solid #ddd; }\n" +
                "        .order-details .total { font-weight: bold; color: #FF8C00; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>✅ Pagamento Confirmado</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <h2>Olá, %s!</h2>\n" +
                "            <p>Seu pagamento foi confirmado e seu pedido já está sendo preparado!</p>\n" +
                "            \n" +
                "            <div class=\"info-box\">\n" +
                "                <h3>📋 Detalhes do Pedido</h3>\n" +
                "                <table class=\"order-details\">\n" +
                "                    <tr>\n" +
                "                        <td><strong>Número do Pedido:</strong></td>\n" +
                "                        <td>#%d</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td><strong>Status:</strong></td>\n" +
                "                        <td><span class=\"status\">✅ PAGAMENTO CONFIRMADO</span></td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td><strong>ID da Transação:</strong></td>\n" +
                "                        <td>%d</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td><strong>Valor Total:</strong></td>\n" +
                "                        <td class=\"total\">R$ %.2f</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td><strong>Data:</strong></td>\n" +
                "                        <td>%s</td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </div>\n" +
                "            \n" +
                "            <p>Seu pedido está em preparação e em breve será entregue. Você pode acompanhar o status na sua conta.</p>\n" +
                "            \n" +
                "            <p><strong>🕒 Tempo estimado de entrega:</strong> 30-45 minutos</p>\n" +
                "            \n" +
                "            <p>Em caso de dúvidas, entre em contato conosco.</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>🍔 <strong>BurgerQueen</strong> - Os melhores hambúrgueres da cidade!</p>\n" +
                "            <p>© 2024 BurgerQueen. Todos os direitos reservados.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return String.format(
                template,
                userName,
                orderId,
                transactionId,
                total,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
    }
}





package com.liaw.dev.BurgerQueen.service;

import com.liaw.dev.BurgerQueen.dto.dtos.OrderDTO;
import com.liaw.dev.BurgerQueen.dto.request.OrderItemRequest;
import com.liaw.dev.BurgerQueen.dto.request.OrderRequest;
import com.liaw.dev.BurgerQueen.dto.request.RemoveItemRequest;
import com.liaw.dev.BurgerQueen.entity.*;
import com.liaw.dev.BurgerQueen.enums.OrderStatus;
import com.liaw.dev.BurgerQueen.exception.exceptions.*;
import com.liaw.dev.BurgerQueen.mapper.OrderMapper;
import com.liaw.dev.BurgerQueen.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper mapper;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public Long getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt){
            Object idClaim = jwt.getClaim("Id");
            if (idClaim != null){
                return Long.valueOf(idClaim.toString());
            }
        }
        throw new UserNotVerifyException("Usuário não verificado.");
    }

    public String getCurrentUserName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt){
            Object nameClaim = jwt.getClaim("Name");
            if (nameClaim != null){
                return nameClaim.toString();
            }
        }
        throw new UserNotVerifyException("Usuário não verificado.");
    }

    public List<OrderDTO> listOrderByUser(){
        List<Order> orders = orderRepository.findByUserIdAndStatus(getCurrentUserId(), OrderStatus.PREPARING);
        return orders.stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public OrderDTO createOrder(OrderRequest request) {
        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));

        Order order = orderRepository.findByUserAndStatus(user, OrderStatus.PENDING)
                .orElseGet(() -> {
                    Order newOrder = new Order();
                    newOrder.setUser(user);
                    newOrder.setItems(new ArrayList<>()); // Inicializa a lista apenas no objeto NOVO
                    newOrder.setStatus(OrderStatus.PENDING);
                    newOrder.setAmount(BigDecimal.ZERO);
                    return newOrder;
                });

        List<Coupon> availableCoupons = new ArrayList<>();
        BigDecimal totalOrder = order.getAmount() != null ? order.getAmount() : BigDecimal.ZERO;

        if (request.getCouponCode() != null && !request.getCouponCode().isEmpty()) {
            availableCoupons = couponRepository.findAllByCodeInAndUserAndUsedFalse(
                    request.getCouponCode(), user
            );

            if (availableCoupons.isEmpty()) {
                throw new CouponNotFoundException("Cupom não encontrado ou já utilizado.");
            }
            List<Coupon> couponStream = availableCoupons.stream()
                    .filter(coupon -> LocalDateTime.now().isAfter(coupon.getExpiredAt()))
                    .toList();
            if (!couponStream.isEmpty()){
                throw new CouponExpiredException("Cupom expirado.");
            }
        }

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado."));

            Optional<Coupon> couponForThisProduct = availableCoupons.stream()
                    .filter(c -> c.getProduct().getId().equals(product.getId()))
                    .filter(c -> LocalDateTime.now().isBefore(c.getExpiredAt()))
                    .findFirst();

            if (couponForThisProduct.isPresent() && itemRequest.getQuantity() > 0) {
                Coupon coupon = couponForThisProduct.get();
                BigDecimal discountFactor = new BigDecimal(coupon.getDiscountPercentage().toString()).divide(new BigDecimal("100"));
                BigDecimal discountValue = product.getPrice().multiply(discountFactor);
                BigDecimal promotionalPrice = product.getPrice().subtract(discountValue);

                OrderItem promotionalItem = new OrderItem(product, 1, promotionalPrice, order);
                order.getItems().add(promotionalItem);
                totalOrder = totalOrder.add(promotionalPrice.setScale(2, RoundingMode.HALF_UP));

                if (itemRequest.getQuantity() > 1) {
                    int remaining = itemRequest.getQuantity() - 1;
                    OrderItem regularItem = new OrderItem(product, remaining, product.getPrice(), order);
                    order.getItems().add(regularItem);
                    totalOrder = totalOrder.add(product.getPrice().multiply(new BigDecimal(remaining)));
                }

                coupon.setUsed(true);
                couponRepository.save(coupon);
                availableCoupons.remove(coupon);
            } else {
                OrderItem orderItem = new OrderItem(product, itemRequest.getQuantity(), product.getPrice(), order);
                order.getItems().add(orderItem);

                BigDecimal subtotal = product.getPrice().multiply(new BigDecimal(itemRequest.getQuantity()));
                totalOrder = totalOrder.add(subtotal);
            }
        }

        order.setAmount(totalOrder.setScale(2, RoundingMode.HALF_UP));
        orderRepository.save(order);
        return mapper.toDTO(order);
    }

    public void completeOrder(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new OrderNotFoundException("Pedido não encontrado"));
        order.setStatus(OrderStatus.COMPLETE);
        order.setId(id);
        orderRepository.save(order);
    }

    @Transactional
    public OrderDTO removeItem(RemoveItemRequest request){
        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(()-> new UserNotFoundException("Usuário não encontrado"));
        Order order = orderRepository.findByUserAndStatus(user, OrderStatus.PENDING)
                .orElseThrow(()-> new OrderNotFoundException("Pedido não encontrado"));
        order.getItems().removeIf(item-> item.getProduct().getId().equals(request.productId()));
        BigDecimal newTotal = order.getItems().stream()
                .map(OrderItem::getPriceAtTime)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setAmount(newTotal.setScale(2, RoundingMode.HALF_UP));

        orderRepository.save(order);
        return mapper.toDTO(order);
    }

    public OrderDTO getCurrentOrder(){
        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));
        Order order = orderRepository.findByUserAndStatus(user, OrderStatus.PENDING)
                .orElseThrow(()-> new OrderNotFoundException("Pedido não encontrado"));
        return mapper.toDTO(order);
    }

    public List<OrderDTO> getCompletedOrder(){
        Long userId = getCurrentUserId();
        System.out.println("DEBUG: ID do usuário autenticado no Spring: " + userId);
        List<Order> order = orderRepository.findByUserIdAndStatus(userId, OrderStatus.COMPLETE);
        return order.stream().map(mapper::toDTO).toList();
    }

    public OrderDTO getPreparingOrder(){
        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));
        Order order = orderRepository.findByUserAndStatus(user, OrderStatus.PREPARING)
                .orElseThrow(()-> new OrderNotFoundException("Pedido não encontrado"));
        return mapper.toDTO(order);
    }

    public List<OrderDTO> listOrders(){
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(mapper::toDTO).toList();
    }
    
    public OrderDTO findById(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new OrderNotFoundException("Pedido não encontrado."));
        return mapper.toDTO(order);

    }

}

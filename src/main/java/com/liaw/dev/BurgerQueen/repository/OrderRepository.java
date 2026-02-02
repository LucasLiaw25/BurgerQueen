package com.liaw.dev.BurgerQueen.repository;

import com.liaw.dev.BurgerQueen.entity.Order;
import com.liaw.dev.BurgerQueen.entity.User;
import com.liaw.dev.BurgerQueen.enums.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByPaymentId(Long paymentId);
    List<Order> findByUserIdAndStatus(Long id, OrderStatus status);
    Optional<Order> findByUserAndStatus(User user, OrderStatus status);
    List<Order> findAllByUserAndStatus(User user, OrderStatus status);
}

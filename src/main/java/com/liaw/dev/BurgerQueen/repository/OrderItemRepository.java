package com.liaw.dev.BurgerQueen.repository;

import com.liaw.dev.BurgerQueen.entity.Order;
import com.liaw.dev.BurgerQueen.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findByOrderId(Long orderId);
}

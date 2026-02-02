package com.liaw.dev.BurgerQueen.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private Integer quantity;
    private BigDecimal priceAtTime;

    public OrderItem(Product product, Integer quantity, BigDecimal priceAtTime, Order order) {
        this.product = product;
        this.quantity = quantity;
        this.priceAtTime = priceAtTime;
        this.order = order;
    }
}

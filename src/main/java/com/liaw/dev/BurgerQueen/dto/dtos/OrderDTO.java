package com.liaw.dev.BurgerQueen.dto.dtos;

import com.liaw.dev.BurgerQueen.dto.response.OrderItemResponse;
import com.liaw.dev.BurgerQueen.dto.response.UserResponse;
import com.liaw.dev.BurgerQueen.entity.OrderItem;
import com.liaw.dev.BurgerQueen.entity.Payment;
import com.liaw.dev.BurgerQueen.entity.User;
import com.liaw.dev.BurgerQueen.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private UserResponse user;
    private List<OrderItemResponse> items = new ArrayList<>();
    private BigDecimal amount;
    private OrderStatus status;
}

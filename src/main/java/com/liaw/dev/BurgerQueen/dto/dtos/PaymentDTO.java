package com.liaw.dev.BurgerQueen.dto.dtos;

import com.liaw.dev.BurgerQueen.dto.response.UserResponse;
import com.liaw.dev.BurgerQueen.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private String txid;
    private PaymentStatus status;
    private OrderDTO order;
    private String link;
}

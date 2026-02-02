package com.liaw.dev.BurgerQueen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {
    private String code;
    private LocalDateTime expiresAt;
    private Double discountPercentage;
}

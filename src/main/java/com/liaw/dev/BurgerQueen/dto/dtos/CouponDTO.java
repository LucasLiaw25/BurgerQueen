package com.liaw.dev.BurgerQueen.dto.dtos;

import com.liaw.dev.BurgerQueen.dto.response.ProductResponse;
import com.liaw.dev.BurgerQueen.dto.response.UserResponse;
import com.liaw.dev.BurgerQueen.entity.Product;
import com.liaw.dev.BurgerQueen.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {
    private Long id;
    private UserResponse user;
    private ProductResponse product;
    private String code;
    private Boolean used = false;
    private Double discountPercentage;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime expiresAt;
}

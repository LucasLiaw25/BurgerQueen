package com.liaw.dev.BurgerQueen.repository;

import com.liaw.dev.BurgerQueen.entity.Coupon;
import com.liaw.dev.BurgerQueen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon findByUserId(Long userId);
    Optional<Coupon> findByCodeAndUserAndUsedFalse(String code, User user);
    List<Coupon> findAllByCodeInAndUserAndUsedFalse(List<String> codes, User user);
    Optional<Coupon> findByCode(String code);
    Optional<Coupon> findByCodeAndUsedFalse(String code);
}

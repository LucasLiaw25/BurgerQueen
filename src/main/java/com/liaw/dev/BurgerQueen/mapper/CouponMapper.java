package com.liaw.dev.BurgerQueen.mapper;

import com.liaw.dev.BurgerQueen.dto.dtos.CouponDTO;
import com.liaw.dev.BurgerQueen.dto.response.CouponProductResponse;
import com.liaw.dev.BurgerQueen.dto.response.CouponResponse;
import com.liaw.dev.BurgerQueen.dto.response.ProductResponse;
import com.liaw.dev.BurgerQueen.dto.response.UserResponse;
import com.liaw.dev.BurgerQueen.entity.Coupon;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public CouponDTO toDTO(Coupon coupon){
        return new CouponDTO(
                coupon.getId(),
                new UserResponse(
                        coupon.getUser().getId(),
                        coupon.getUser().getName(),
                        coupon.getUser().getEmail()
                ),
                new ProductResponse(
                        coupon.getProduct().getId(),
                        coupon.getProduct().getName(),
                        coupon.getProduct().getImageUrl(),
                        coupon.getProduct().getPrice()
                ),
                coupon.getCode(),
                coupon.getUsed(),
                coupon.getDiscountPercentage(),
                coupon.getCreatedAt(),
                coupon.getExpiredAt()
        );
    }
    private ProductResponse product;
    public CouponProductResponse toCouponProduct(Coupon coupon){
        return new CouponProductResponse(
                new ProductResponse(
                        coupon.getProduct().getId(),
                        coupon.getProduct().getName(),
                        coupon.getProduct().getImageUrl(),
                        coupon.getProduct().getPrice()
                )
        );
    }

    public CouponResponse toResponse(Coupon coupon){
        return new CouponResponse(
                coupon.getCode(),
                coupon.getExpiredAt(),
                coupon.getDiscountPercentage()
        );
    }

}

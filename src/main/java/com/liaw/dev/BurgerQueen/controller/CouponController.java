package com.liaw.dev.BurgerQueen.controller;

import com.liaw.dev.BurgerQueen.dto.dtos.CouponDTO;
import com.liaw.dev.BurgerQueen.dto.request.CouponProductRequest;
import com.liaw.dev.BurgerQueen.dto.request.CouponRequest;
import com.liaw.dev.BurgerQueen.dto.response.CouponProductResponse;
import com.liaw.dev.BurgerQueen.dto.response.CouponResponse;
import com.liaw.dev.BurgerQueen.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/coupons/")
public class CouponController {

    private final CouponService service;

    @PostMapping
    public ResponseEntity<CouponResponse> generateCoupon(@RequestBody CouponRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createCoupon(request));
    }

    @GetMapping("code")
    public ResponseEntity<CouponProductResponse> getProductByCoupon(@RequestParam String code){
        return ResponseEntity.ok(service.getProductByCoupon(new CouponProductRequest(code)));
    }

    @GetMapping
    public ResponseEntity<List<CouponDTO>> listCoupons(){
        return ResponseEntity.ok(service.listCoupons());
    }

    @GetMapping("user")
    public ResponseEntity<CouponDTO> findByUserId(){
        return ResponseEntity.ok(service.findByUserId());
    }

}

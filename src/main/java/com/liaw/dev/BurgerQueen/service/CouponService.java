package com.liaw.dev.BurgerQueen.service;

import com.liaw.dev.BurgerQueen.dto.dtos.CouponDTO;
import com.liaw.dev.BurgerQueen.dto.request.CouponProductRequest;
import com.liaw.dev.BurgerQueen.dto.request.CouponRequest;
import com.liaw.dev.BurgerQueen.dto.response.CouponProductResponse;
import com.liaw.dev.BurgerQueen.dto.response.CouponResponse;
import com.liaw.dev.BurgerQueen.entity.Coupon;
import com.liaw.dev.BurgerQueen.entity.Product;
import com.liaw.dev.BurgerQueen.entity.User;
import com.liaw.dev.BurgerQueen.exception.exceptions.CouponNotFoundException;
import com.liaw.dev.BurgerQueen.exception.exceptions.ProductNotFoundException;
import com.liaw.dev.BurgerQueen.exception.exceptions.UserNotFoundException;
import com.liaw.dev.BurgerQueen.mapper.CouponMapper;
import com.liaw.dev.BurgerQueen.repository.CouponRepository;
import com.liaw.dev.BurgerQueen.repository.ProductRepository;
import com.liaw.dev.BurgerQueen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CouponService {


    private final CouponMapper mapper;
    private final OrderService orderService;
    private final CouponRepository repository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CouponResponse createCoupon(CouponRequest request){
        User user = userRepository.findById(orderService.getCurrentUserId())
                .orElseThrow(()-> new UserNotFoundException("Usuário não encontrado."));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(()-> new ProductNotFoundException("Produto não encontrado."));
        Coupon coupon = new Coupon();
        coupon.setUser(user);
        coupon.setProduct(product);
        coupon.setCode(generateCode());
        coupon.setDiscountPercentage(25.00);
        coupon.setCreatedAt(LocalDateTime.now());
        coupon.setExpiredAt(LocalDateTime.now().plusDays(1));
        repository.save(coupon);
        return mapper.toResponse(coupon);
    }

    public CouponDTO findByUserId(){
        Coupon coupon = repository.findByUserId(orderService.getCurrentUserId());
        return mapper.toDTO(coupon);
    }

    public List<CouponDTO> listCoupons(){
        List<Coupon> coupons = repository.findAll();
        return coupons.stream().map(mapper::toDTO).toList();
    }

    public CouponProductResponse getProductByCoupon(CouponProductRequest request){
        Coupon coupon = repository.findByCodeAndUsedFalse(request.code())
                .orElseThrow(()-> new CouponNotFoundException("Cupom inválido ou expirado"));
        return mapper.toCouponProduct(coupon);
    }

    private String generateCode(){
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        String alphaNumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < 8; i++) {
            code = code.append(alphaNumeric.charAt(random.nextInt(alphaNumeric.length())));
        }
        return code.toString();
    }


}

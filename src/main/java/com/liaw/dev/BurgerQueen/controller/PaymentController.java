package com.liaw.dev.BurgerQueen.controller;

import com.liaw.dev.BurgerQueen.dto.dtos.PaymentDTO;
import com.liaw.dev.BurgerQueen.dto.request.PixRequest;
import com.liaw.dev.BurgerQueen.entity.Payment;
import com.liaw.dev.BurgerQueen.enums.PaymentStatus;
import com.liaw.dev.BurgerQueen.pix.PixService;
import com.liaw.dev.BurgerQueen.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/payments/")
public class PaymentController {

    private final PixService service;
    private final PaymentRepository repository;

    @PostMapping
    public ResponseEntity createPixPayment(@RequestBody PixRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createPayment(request));
    }


}

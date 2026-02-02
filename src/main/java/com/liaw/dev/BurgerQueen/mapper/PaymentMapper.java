package com.liaw.dev.BurgerQueen.mapper;

import com.liaw.dev.BurgerQueen.dto.dtos.PaymentDTO;
import com.liaw.dev.BurgerQueen.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    @Autowired
    private OrderMapper orderMapper;

    public PaymentDTO toDTO(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getTxid(),
                payment.getStatus(),
                orderMapper.toDTO(payment.getOrder()),
                payment.getPaymentLink()
        );
    }
}

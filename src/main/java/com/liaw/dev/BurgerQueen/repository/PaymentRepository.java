package com.liaw.dev.BurgerQueen.repository;

import com.liaw.dev.BurgerQueen.entity.Payment;
import com.liaw.dev.BurgerQueen.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStatus(PaymentStatus status);
    Optional<Payment> findByTxid(String txid);
}

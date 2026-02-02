package com.liaw.dev.BurgerQueen.repository;

import com.liaw.dev.BurgerQueen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndCpf(String email, String cpf);
    Optional<User> findByCode(String code);
}

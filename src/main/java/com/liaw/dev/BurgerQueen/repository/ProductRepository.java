package com.liaw.dev.BurgerQueen.repository;

import com.liaw.dev.BurgerQueen.entity.Product;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, QueryByExampleExecutor<Product> {
    Optional<Product> findByNameAndDescription(String name, String description);
    List<Product> findByCategory_Id(Long categoryId);
}

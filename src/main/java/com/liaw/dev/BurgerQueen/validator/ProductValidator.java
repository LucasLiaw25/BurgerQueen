package com.liaw.dev.BurgerQueen.validator;

import com.liaw.dev.BurgerQueen.entity.Product;
import com.liaw.dev.BurgerQueen.exception.exceptions.ProductExistException;
import com.liaw.dev.BurgerQueen.exception.exceptions.ProductNotFoundException;
import com.liaw.dev.BurgerQueen.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final ProductRepository repository;

    public void validateProductId(Long id){
        Optional<Product> product = repository.findById(id);
        if (product.isEmpty()){
            throw new ProductNotFoundException("Produto com ID " + id + " não encontrado.");
        }
    }

    public void validateCreateProduct(String name, String description){
        Optional<Product> product = repository.findByNameAndDescription(name, description);
        if (product.isPresent()){
            throw new ProductExistException("Produto " + name + " já cadastrado.");
        }
    }

}

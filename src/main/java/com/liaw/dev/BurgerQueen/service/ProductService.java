package com.liaw.dev.BurgerQueen.service;

import com.liaw.dev.BurgerQueen.dto.dtos.ProductDTO;
import com.liaw.dev.BurgerQueen.entity.Product;
import com.liaw.dev.BurgerQueen.mapper.ProductMapper;
import com.liaw.dev.BurgerQueen.repository.ProductRepository;
import com.liaw.dev.BurgerQueen.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper mapper;
    private final ProductValidator validator;
    private final ProductRepository repository;

    public ProductDTO createProduct(ProductDTO dto){
        validator.validateCreateProduct(dto.getName(), dto.getDescription());
        Product product = mapper.toEntity(dto);
        repository.save(product);
        return mapper.toDTO(product);
    }

    public List<ProductDTO> listProducts(){
        List<Product> products = repository.findAll();
        return products.stream().map(mapper::toDTO).toList();
    }

    public ProductDTO findById(Long id){
        validator.validateProductId(id);
        Product product = repository.findById(id).get();
        return mapper.toDTO(product);
    }

    public List<ProductDTO> findByCategoryId(Long categoryId){
        List<Product> products = repository.findByCategory_Id(categoryId);
        return products.stream().map(mapper::toDTO).toList();
    }

    public ProductDTO updateProduct(Long id, ProductDTO dto){
        validator.validateProductId(id);
        Product product = mapper.toEntity(dto);
        product.setId(dto.getId());
        repository.save(product);
        return mapper.toDTO(product);
    }

    public List<ProductDTO> searchProduct(String name, String description){
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreNullValues();
        Example<Product> example = Example.of(product, matcher);
        List<Product> products = repository.findAll(example);
        return products.stream().map(mapper::toDTO).toList();
    }

    public void deleteProduct(Long id){
        validator.validateProductId(id);
        repository.deleteById(id);
    }

}

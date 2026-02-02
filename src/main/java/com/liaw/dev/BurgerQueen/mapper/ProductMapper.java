package com.liaw.dev.BurgerQueen.mapper;

import com.liaw.dev.BurgerQueen.dto.dtos.CategoryDTO;
import com.liaw.dev.BurgerQueen.dto.dtos.ProductDTO;
import com.liaw.dev.BurgerQueen.entity.Category;
import com.liaw.dev.BurgerQueen.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product){
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                new CategoryDTO(
                        product.getCategory().getId(),
                        product.getCategory().getName(),
                        product.getCategory().getSlug()
                ),
                product.getRating(),
                product.getRatingCount()
        );
    }

    public Product toEntity(ProductDTO product){
        return new Product(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getRating(),
                product.getRatingCount(),
                new Category(
                        product.getCategory().getId(),
                        product.getCategory().getName(),
                        product.getCategory().getSlug()
                )
        );
    }

}

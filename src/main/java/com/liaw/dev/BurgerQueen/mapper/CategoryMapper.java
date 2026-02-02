package com.liaw.dev.BurgerQueen.mapper;

import com.liaw.dev.BurgerQueen.dto.dtos.CategoryDTO;
import com.liaw.dev.BurgerQueen.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category category){
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getSlug()
        );
    }

    public Category toEntity(CategoryDTO category){
        return new Category(
                category.getId(),
                category.getName(),
                category.getSlug()
        );
    }

}

package com.liaw.dev.BurgerQueen.validator;

import com.liaw.dev.BurgerQueen.entity.Category;
import com.liaw.dev.BurgerQueen.exception.exceptions.CategoryExistException;
import com.liaw.dev.BurgerQueen.exception.exceptions.CategoryNotFoundException;
import com.liaw.dev.BurgerQueen.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryValidator {

    private final CategoryRepository repository;

    public void validateCategoryId(Long id){
        Optional<Category> category = repository.findById(id);
        if (category.isEmpty()){
            throw new CategoryNotFoundException("Categoria não encontrada");
        }
    }

    public void validateCategoryName(String name){
        Optional<Category> category = repository.findByName(name);
        if (category.isPresent()){
            throw new CategoryExistException("Categoria com nome " + name + " já cadastrada.");
        }
    }

}

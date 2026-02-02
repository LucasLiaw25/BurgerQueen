package com.liaw.dev.BurgerQueen.service;

import com.liaw.dev.BurgerQueen.dto.dtos.CategoryDTO;
import com.liaw.dev.BurgerQueen.entity.Category;
import com.liaw.dev.BurgerQueen.mapper.CategoryMapper;
import com.liaw.dev.BurgerQueen.repository.CategoryRepository;
import com.liaw.dev.BurgerQueen.validator.CategoryValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper mapper;
    private final CategoryValidator validator;
    private final CategoryRepository repository;

    public CategoryDTO createCategory(CategoryDTO dto){
        validator.validateCategoryName(dto.getName());
        Category category = mapper.toEntity(dto);
        repository.save(category);
        return mapper.toDTO(category);
    }

    public List<CategoryDTO> listCategories(){
        List<Category> categories = repository.findAll();
        return categories.stream().map(mapper::toDTO).toList();
    }

    public CategoryDTO findById(Long id){
        validator.validateCategoryId(id);
        Category category = repository.findById(id).get();
        return mapper.toDTO(category);
    }

    public void deleteCategory(Long id){
        validator.validateCategoryId(id);
        repository.deleteById(id);
    }

}

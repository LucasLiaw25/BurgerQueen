package com.liaw.dev.BurgerQueen.controller;

import com.liaw.dev.BurgerQueen.config.annotation.AdminAll;
import com.liaw.dev.BurgerQueen.dto.dtos.CategoryDTO;
import com.liaw.dev.BurgerQueen.dto.response.Message;
import com.liaw.dev.BurgerQueen.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/categories/")
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createCategory(dto));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> listCategories(){
        return ResponseEntity.ok(service.listCategories());
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @AdminAll
    @DeleteMapping("{id}")
    public Message deleteCategory(@PathVariable Long id){
        service.deleteCategory(id);
        return new Message(
                HttpStatus.OK.value(),
                "Categoria deletada com sucesso."
        );
    }

}

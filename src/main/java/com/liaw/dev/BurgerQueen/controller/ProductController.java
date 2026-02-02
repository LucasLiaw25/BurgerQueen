package com.liaw.dev.BurgerQueen.controller;

import com.liaw.dev.BurgerQueen.config.annotation.AdminAll;
import com.liaw.dev.BurgerQueen.dto.dtos.ProductDTO;
import com.liaw.dev.BurgerQueen.dto.response.Message;
import com.liaw.dev.BurgerQueen.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/products/")
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createProduct(dto));
    }

    @GetMapping("search")
    public ResponseEntity<List<ProductDTO>> searchProduct(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description
    ){
        return ResponseEntity.ok(service.searchProduct(name, description));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> listProducts(){
        return ResponseEntity.ok(service.listProducts());
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @AdminAll
    @PutMapping("{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id, @RequestBody ProductDTO dto
    ){
        return ResponseEntity.ok(service.updateProduct(id, dto));
    }

    @GetMapping("category/{id}")
    public ResponseEntity<List<ProductDTO>> findByCategoryId(@PathVariable Long id){
        return ResponseEntity.ok(service.findByCategoryId(id));
    }

    @AdminAll
    @DeleteMapping("{id}")
    public Message deleteProduct(@PathVariable Long id){
        service.deleteProduct(id);
        return new Message(
                HttpStatus.OK.value(),
                "Produto deletado com sucesso."
        );
    }

}

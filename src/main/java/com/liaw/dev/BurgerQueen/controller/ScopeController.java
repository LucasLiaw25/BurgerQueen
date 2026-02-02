package com.liaw.dev.BurgerQueen.controller;

import com.liaw.dev.BurgerQueen.config.annotation.AdminAll;
import com.liaw.dev.BurgerQueen.dto.dtos.ScopeDTO;
import com.liaw.dev.BurgerQueen.dto.response.Message;
import com.liaw.dev.BurgerQueen.service.ScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scopes/")
public class ScopeController {

    private final ScopeService service;

    @AdminAll
    @PostMapping
    public ResponseEntity<ScopeDTO> createScope(@RequestBody ScopeDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createScope(dto));
    }

    @GetMapping
    public ResponseEntity<List<ScopeDTO>> listScope(){
        return ResponseEntity.ok(service.listScope());
    }

    @GetMapping("{id}")
    public ResponseEntity<ScopeDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @AdminAll
    @DeleteMapping("{id}")
    public ResponseEntity<Message> deleteScope(@PathVariable Long id){
        service.deleteScope(id);
        return ResponseEntity.ok(
                new Message(
                        HttpStatus.OK.value(),
                        "Escopo deletado com sucesso."
                )
        );
    }

}

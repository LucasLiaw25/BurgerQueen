package com.liaw.dev.BurgerQueen.controller;

import com.liaw.dev.BurgerQueen.config.annotation.AdminAll;
import com.liaw.dev.BurgerQueen.dto.dtos.TokenDTO;
import com.liaw.dev.BurgerQueen.dto.dtos.UserDTO;
import com.liaw.dev.BurgerQueen.dto.request.ActivateRequest;
import com.liaw.dev.BurgerQueen.dto.request.LoginRequest;
import com.liaw.dev.BurgerQueen.dto.request.UserRequest;
import com.liaw.dev.BurgerQueen.dto.response.Message;
import com.liaw.dev.BurgerQueen.entity.User;
import com.liaw.dev.BurgerQueen.repository.UserRepository;
import com.liaw.dev.BurgerQueen.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/")
public class UserController {

    private final UserRepository repository;
    private final UserService service;

    @RequestMapping(value = "validate", method = RequestMethod.HEAD)
    public ResponseEntity<Void> validateToken(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("register")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createUser(request));
    }


    @PostMapping("activate")
    public ResponseEntity<Message> activateUser(@RequestBody ActivateRequest request){
        System.out.println("Código recebido: " + request.code());
        service.activateUser(request);
        return ResponseEntity.ok(
                new Message(
                        HttpStatus.OK.value(),
                        "Conta ativada com sucesso."
                )
        );
    }

    @PostMapping("login")
    public TokenDTO login(@RequestBody LoginRequest request){
        return service.login(request);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> listUsers(){
        return ResponseEntity.ok(service.listUsers());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDTO> findByid(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserRequest request){
        return ResponseEntity.ok(service.updateUser(id, request));
    }

    @AdminAll
    @DeleteMapping("{id}")
    public ResponseEntity<Message> deleteUser(@PathVariable Long id){
        service.deleteUser(id);
        return ResponseEntity.ok(
                new Message(
                        HttpStatus.OK.value(),
                        "Usuário com id: " + id + " deletado com sucesso."
                )
        );
    }

}

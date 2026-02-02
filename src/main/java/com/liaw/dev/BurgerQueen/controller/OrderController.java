package com.liaw.dev.BurgerQueen.controller;

import com.liaw.dev.BurgerQueen.config.annotation.AdminAll;
import com.liaw.dev.BurgerQueen.dto.dtos.OrderDTO;
import com.liaw.dev.BurgerQueen.dto.request.OrderItemRequest;
import com.liaw.dev.BurgerQueen.dto.request.OrderRequest;
import com.liaw.dev.BurgerQueen.dto.request.RemoveItemRequest;
import com.liaw.dev.BurgerQueen.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orders/")
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createOrder(request));
    }

    @DeleteMapping("remove-item")
    public ResponseEntity<OrderDTO> removeItem(@RequestBody RemoveItemRequest request){
        return ResponseEntity.ok(service.removeItem(request));
    }

    @GetMapping("pending")
    public ResponseEntity<OrderDTO> getCurrentOrder(){
        return ResponseEntity.ok(service.getCurrentOrder());
    }

    @PostMapping("complete/{id}")
    public String completeOrder(@PathVariable Long id){
        service.completeOrder(id);
        return "Pedido completo. Pronto para entregar pro cliente!";
    }

    @GetMapping("preparing")
    public ResponseEntity<OrderDTO> getPreparingOrder(){
        return ResponseEntity.ok(service.getPreparingOrder());
    }

    @GetMapping("completed")
    public ResponseEntity<List<OrderDTO>> getCompletedOrder(){
        return ResponseEntity.ok(service.getCompletedOrder());
    }

    @GetMapping("username")
    public String getUserName(){
        return service.getCurrentUserName();
    }

    @GetMapping("user")
    public ResponseEntity<List<OrderDTO>> listOrderByUser(){
        return ResponseEntity.ok(service.listOrderByUser());
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> listOrders(){
        return ResponseEntity.ok(service.listOrders());
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

}

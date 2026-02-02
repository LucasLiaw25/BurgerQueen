package com.liaw.dev.BurgerQueen.dto.request;

import com.liaw.dev.BurgerQueen.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private List<OrderItemRequest> items = new ArrayList<>();
    private List<String> couponCode;
}

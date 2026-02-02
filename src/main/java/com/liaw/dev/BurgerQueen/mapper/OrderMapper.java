package com.liaw.dev.BurgerQueen.mapper;

import com.liaw.dev.BurgerQueen.dto.dtos.OrderDTO;
import com.liaw.dev.BurgerQueen.dto.response.OrderItemResponse;
import com.liaw.dev.BurgerQueen.dto.response.ProductResponse;
import com.liaw.dev.BurgerQueen.dto.response.UserResponse;
import com.liaw.dev.BurgerQueen.entity.Order;
import com.liaw.dev.BurgerQueen.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderMapper {

    public OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setAmount(order.getAmount());
        dto.setStatus(order.getStatus());

        if (order.getUser() != null) {
            dto.setUser(new UserResponse(
                    order.getUser().getId(),
                    order.getUser().getName(),
                    order.getUser().getEmail()
            ));
        }

        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(this::toItemResponse)
                .toList();

        dto.setItems(itemResponses);

        return dto;
    }

    public OrderItemResponse toItemResponse(OrderItem item) {
        OrderItemResponse resp = new OrderItemResponse();
        resp.setId(item.getId());
        resp.setQuantity(item.getQuantity());
        resp.setPriceAtTime(item.getPriceAtTime());
        if (item.getProduct() != null) {
            resp.setProduct(new ProductResponse(
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getProduct().getImageUrl(),
                    item.getProduct().getPrice()
            ));
        }

        return resp;
    }
}

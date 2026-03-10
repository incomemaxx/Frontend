package org.example.matching.api.dto;

import org.example.matching.model.Order;
import org.example.matching.model.OrderSide;

import java.util.UUID;

public class OrderMapper {
    public static Order toDomain(OrderRequest request) {
        String orderId;
        if ("HOUSE_BOT".equals(request.getUserId())) {
            orderId = "HOUSE_BOT-" + request.getInstrument() + "-" + request.getSide() + "-" + System.currentTimeMillis();
        } else {
            orderId = UUID.randomUUID().toString();
        }
        
        Order order = new Order(
                orderId,
                request.getUserId(),
                request.getPrice(),
                (int) request.getQuantity(),
                System.currentTimeMillis(),
                OrderSide.valueOf(request.getSide().toUpperCase())
        );
        order.setInstrument(request.getInstrument());
        return order;
    }
}
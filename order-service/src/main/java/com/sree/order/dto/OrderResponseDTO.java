package com.sree.order.dto;

import java.util.UUID;

public record OrderResponseDTO(
        UUID orderId,
        Integer userId,
        Integer productId,
        Double amount,
        Integer quantity,
        OrderStatus status
) {
}

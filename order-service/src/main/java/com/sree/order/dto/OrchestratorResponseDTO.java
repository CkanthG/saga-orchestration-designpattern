package com.sree.order.dto;

import java.util.UUID;

public record OrchestratorResponseDTO(
        UUID orderId,
        Integer userId,
        Integer productId,
        Double amount,
        Integer quantity,
        OrderStatus status
) {
}

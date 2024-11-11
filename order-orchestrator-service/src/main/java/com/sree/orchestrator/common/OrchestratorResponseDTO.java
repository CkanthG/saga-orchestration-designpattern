package com.sree.orchestrator.common;

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

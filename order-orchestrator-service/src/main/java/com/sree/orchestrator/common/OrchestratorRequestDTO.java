package com.sree.orchestrator.common;

import java.util.UUID;

public record OrchestratorRequestDTO(
        UUID orderId,
        Integer userId,
        Integer productId,
        Integer quantity,
        Double amount
) {
}

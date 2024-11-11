package com.sree.orchestrator.common;

import java.util.UUID;

public record InventoryResponseDTO(
        Integer userId,
        Integer productId,
        UUID orderId,
        Integer quantity,
        InventoryStatus status
) {
}

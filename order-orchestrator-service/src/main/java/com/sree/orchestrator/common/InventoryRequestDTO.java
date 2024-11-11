package com.sree.orchestrator.common;

import java.util.UUID;

public record InventoryRequestDTO(
        Integer userId,
        Integer productId,
        UUID orderId,
        Integer quantity
) {
}

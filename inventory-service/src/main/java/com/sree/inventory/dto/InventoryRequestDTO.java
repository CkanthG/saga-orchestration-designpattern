package com.sree.inventory.dto;

import java.util.UUID;

public record InventoryRequestDTO(
        Integer userId,
        Integer productId,
        UUID orderId,
        Integer quantity
) {
}

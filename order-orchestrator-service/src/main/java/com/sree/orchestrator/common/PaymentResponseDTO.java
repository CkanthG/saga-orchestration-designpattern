package com.sree.orchestrator.common;

import java.util.UUID;

public record PaymentResponseDTO(
        Integer userId,
        UUID orderId,
        Double amount,
        Integer quantity,
        PaymentStatus status
) {
}

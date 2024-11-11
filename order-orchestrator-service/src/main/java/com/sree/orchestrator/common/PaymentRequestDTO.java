package com.sree.orchestrator.common;

import java.util.UUID;

public record PaymentRequestDTO(
        Integer userId,
        UUID orderId,
        Double amount,
        Integer quantity
) {
}

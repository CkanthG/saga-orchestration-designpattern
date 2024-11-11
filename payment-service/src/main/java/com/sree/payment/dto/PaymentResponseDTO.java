package com.sree.payment.dto;

import java.util.UUID;

public record PaymentResponseDTO(
        Integer userId,
        UUID orderId,
        Double amount,
        Integer quantity,
        PaymentStatus status
) {
}

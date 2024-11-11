package com.sree.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
        private UUID orderId;
        private Integer userId;
        private Integer productId;
        private Integer quantity;
        private Double amount;
}

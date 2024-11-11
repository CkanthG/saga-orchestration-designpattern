package com.sree.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "products_tbl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    Integer productId;
    String productName;
    Double price;
}

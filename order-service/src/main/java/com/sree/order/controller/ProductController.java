package com.sree.order.controller;

import com.sree.order.dto.ProductRequestDTO;
import com.sree.order.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<Void> addProducts(@RequestBody ProductRequestDTO productRequestDTO) {
        productService.addProducts(productRequestDTO);
        return ResponseEntity.ok().build();
    }

}

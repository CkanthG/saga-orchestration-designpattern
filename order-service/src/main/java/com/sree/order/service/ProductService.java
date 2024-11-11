package com.sree.order.service;

import com.sree.order.dto.ProductRequestDTO;
import com.sree.order.entity.Product;
import com.sree.order.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void addProducts(ProductRequestDTO productRequestDTO) {
        Flux<Product> product = productRepository.findByProductName(productRequestDTO.productName());
        product.flatMap(
                p -> {
                    p.setPrice(productRequestDTO.productPrice());
                    return productRepository.save(p);
                }
        ).switchIfEmpty(
                Mono.defer(
                        () -> productRepository.save(
                                new Product(
                            null,
                                productRequestDTO.productName(),
                                productRequestDTO.productPrice()
                            )
                        )
                )
        ).subscribe();
    }
}

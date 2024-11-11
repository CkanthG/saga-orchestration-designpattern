package com.sree.order.service;

import com.sree.order.dto.*;
import com.sree.order.entity.Product;
import com.sree.order.entity.PurchaseOrder;
import com.sree.order.repository.ProductRepository;
import com.sree.order.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProductRepository productRepository;
    private final Sinks.Many<OrchestratorRequestDTO> sinks;

    public Mono<PurchaseOrder> createOrder(OrderRequestDTO orderRequestDTO) {
               return convertDtoToEntity(orderRequestDTO)
                       .flatMap(purchaseOrderRepository::save)
                .doOnNext(p -> orderRequestDTO.setOrderId(p.getId()))
                .doOnNext(e -> emitEvent(orderRequestDTO));
    }

    private void emitEvent(OrderRequestDTO orderRequestDTO) {
        getProductPriceByProductId(orderRequestDTO).subscribe(
                price -> {
                    sinks.tryEmitNext(
                            new OrchestratorRequestDTO(
                                    orderRequestDTO.getOrderId(),
                                    orderRequestDTO.getUserId(),
                                    orderRequestDTO.getProductId(),
                                    orderRequestDTO.getQuantity(),
                                    price
                            )
                    );
                }
        );
    }

    private Mono<PurchaseOrder> convertDtoToEntity(OrderRequestDTO orderRequestDTO) {
        return getProductPriceByProductId(orderRequestDTO)
                .map(price -> {
                    PurchaseOrder purchaseOrder = new PurchaseOrder();
                    purchaseOrder.setUserId(orderRequestDTO.getUserId());
                    purchaseOrder.setPrice(price);
                    purchaseOrder.setProductId(orderRequestDTO.getProductId());
                    purchaseOrder.setQuantity(orderRequestDTO.getQuantity());
                    purchaseOrder.setStatus(OrderStatus.ORDER_CREATED);
                    return purchaseOrder;
                });
    }

    private Mono<Double> getProductPriceByProductId(OrderRequestDTO orderRequestDTO) {
        return productRepository.findById(orderRequestDTO.getProductId())
                .map(Product::getPrice)
                .defaultIfEmpty(0.0);
    }

    public Flux<OrderResponseDTO> getAllOrders() {
        return purchaseOrderRepository.findAll()
                .map(this::convertPurchaseOrderEntityToOrderResponseDTO);
    }

    private OrderResponseDTO convertPurchaseOrderEntityToOrderResponseDTO(PurchaseOrder purchaseOrder) {
        return new OrderResponseDTO(
                purchaseOrder.getId(),
                purchaseOrder.getUserId(),
                purchaseOrder.getProductId(),
                purchaseOrder.getPrice(),
                purchaseOrder.getQuantity(),
                purchaseOrder.getStatus()
        );
    }

    public Mono<Void> updateOrder(OrchestratorResponseDTO responseDTO) {
        return purchaseOrderRepository.findById(responseDTO.orderId())
                .flatMap(purchaseOrder -> {
                    purchaseOrder.setStatus(responseDTO.status());
                    return purchaseOrderRepository.save(purchaseOrder);
                })
                .doOnNext(po -> log.info("Updated Order: {}", po))
                .then();
    }

}

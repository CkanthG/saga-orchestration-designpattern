package com.sree.inventory.service;

import com.sree.inventory.dto.InventoryRequestDTO;
import com.sree.inventory.dto.InventoryResponseDTO;
import com.sree.inventory.dto.InventoryStatus;
import com.sree.inventory.entity.Inventory;
import com.sree.inventory.exception.NotSufficientQuantityAvailableException;
import com.sree.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;

    public String add(InventoryRequestDTO requestDTO) {
        Optional<Inventory> inventoryResponse = productRepository.findById(requestDTO.productId());
        if (inventoryResponse.isEmpty()) {
            Inventory inventory = productRepository.save(
                    new Inventory(
                            null,
                            requestDTO.userId(),
                            requestDTO.orderId(),
                            requestDTO.quantity()
                    )
            );
            return "Items Added Successfully";
        } else {
            inventoryResponse.ifPresent(inventory -> {
                inventory.setQuantity(inventory.getQuantity() + requestDTO.quantity());
                productRepository.save(inventory);
                log.info("Item added back successfully");
            });
            return "Items Updated Successfully";
        }
    }

    public InventoryResponseDTO deduct(InventoryRequestDTO requestDTO) throws NotSufficientQuantityAvailableException {
        Optional<Inventory> inventoryResponse = productRepository.findById(requestDTO.productId());
        if (inventoryResponse.isEmpty()) {
            return new InventoryResponseDTO(
                    requestDTO.userId(),
                    requestDTO.productId(),
                    requestDTO.orderId(),
                    requestDTO.quantity(),
                    InventoryStatus.UNAVAILABLE
            );
        } else {
            Inventory response = inventoryResponse.get();
            if (response.getQuantity() > 0) {
                Inventory inventory = new Inventory();
                inventory.setUserId(requestDTO.userId());
                inventory.setProductId(requestDTO.productId());
                inventory.setOrderId(requestDTO.orderId());
                inventory.setQuantity(response.getQuantity() - requestDTO.quantity());
                productRepository.save(inventory);
                return new InventoryResponseDTO(
                        requestDTO.userId(),
                        requestDTO.productId(),
                        requestDTO.orderId(),
                        requestDTO.quantity(),
                        InventoryStatus.AVAILABLE
                );
            } else {
                throw new NotSufficientQuantityAvailableException("Not sufficient quantity available to order");
            }
        }
    }
}

package com.sree.inventory.controller;

import com.sree.inventory.dto.InventoryRequestDTO;
import com.sree.inventory.dto.InventoryResponseDTO;
import com.sree.inventory.exception.NotSufficientQuantityAvailableException;
import com.sree.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/deduct")
    public ResponseEntity<InventoryResponseDTO> deduct(@RequestBody InventoryRequestDTO requestDTO) {
        return ResponseEntity.ok(inventoryService.deduct(requestDTO));
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody InventoryRequestDTO requestDTO) {
        return ResponseEntity.ok(inventoryService.add(requestDTO));
    }
}

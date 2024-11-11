package com.sree.orchestrator.service;

import com.sree.orchestrator.common.InventoryRequestDTO;
import com.sree.orchestrator.common.InventoryResponseDTO;
import com.sree.orchestrator.common.InventoryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class InventoryStep implements WorkflowStep {

    private final WebClient webClient;
    private final InventoryRequestDTO inventoryRequestDTO;
    private WorkflowStepStatus workflowStepStatus = WorkflowStepStatus.PENDING;

    @Override
    public WorkflowStepStatus getStatus() {
        return workflowStepStatus;
    }

    @Override
    public Mono<Boolean> process() {
        return webClient
                .post()
                .uri("/inventory/deduct")
                .body(BodyInserters.fromValue(inventoryRequestDTO))
                .retrieve()
                .bodyToMono(InventoryResponseDTO.class)
                .map(inv -> inv.status().equals(InventoryStatus.AVAILABLE))
                .doOnNext(i -> workflowStepStatus = i ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> revert() {
        return webClient
                .post()
                .uri("/inventory/add")
                .body(BodyInserters.fromValue(inventoryRequestDTO))
                .retrieve()
                .bodyToMono(Void.class)
                .map(inv -> true)
                .onErrorReturn(false);
    }
}

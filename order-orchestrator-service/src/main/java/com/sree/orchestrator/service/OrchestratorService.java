package com.sree.orchestrator.service;

import com.sree.orchestrator.common.*;
import com.sree.orchestrator.exception.WorkflowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OrchestratorService {

    @Autowired
    @Qualifier("payment")
    public WebClient paymentClient;

    @Autowired
    @Qualifier("inventory")
    public WebClient inventoryClient;

    public Mono<OrchestratorResponseDTO> orderProduct(OrchestratorRequestDTO orchestratorRequestDTO) {
        Workflow orderWorkflow = getOrderWorkflow(orchestratorRequestDTO);

        return  Flux.fromStream(() -> orderWorkflow.getSteps().stream()).flatMap(WorkflowStep::process)
                .handle(((aBoolean, synchronousSink) -> {
                    if (aBoolean) {
                        synchronousSink.next(true);
                    } else {
                        synchronousSink.error(new WorkflowException("Order not processed."));
                    }
                })).then(Mono.fromCallable(() -> getOrchestratorResponseDTO(orchestratorRequestDTO, OrderStatus.ORDER_COMPLETED)))
                .onErrorResume(ex -> revertOrder(orderWorkflow, orchestratorRequestDTO));
    }

    private Mono<OrchestratorResponseDTO> revertOrder(Workflow orderWorkflow, OrchestratorRequestDTO orchestratorRequestDTO) {

        return Flux.fromStream(() -> orderWorkflow.getSteps().stream())
                .filter(wf -> wf.getStatus().equals(WorkflowStepStatus.COMPLETE))
                .flatMap(WorkflowStep::revert)
                .then(Mono.just(getOrchestratorResponseDTO(orchestratorRequestDTO, OrderStatus.ORDER_CANCELLED))
                );
    }

    private OrchestratorResponseDTO getOrchestratorResponseDTO(OrchestratorRequestDTO requestDTO, OrderStatus status) {
        return new OrchestratorResponseDTO(
                requestDTO.orderId(),
                requestDTO.userId(),
                requestDTO.productId(),
                requestDTO.amount(),
                requestDTO.quantity(),
                status
        );
    }

    private Workflow getOrderWorkflow(OrchestratorRequestDTO orchestratorRequestDTO) {
        WorkflowStep paymentStep = new PaymentStep(paymentClient, getPaymentRequestDTO(orchestratorRequestDTO));
        WorkflowStep inventoryStep = new InventoryStep(inventoryClient, getInventoryRequestDTO(orchestratorRequestDTO));
        return new OrderWorkflow(List.of(paymentStep, inventoryStep));
    }

    private InventoryRequestDTO getInventoryRequestDTO(OrchestratorRequestDTO requestDTO) {
        return new InventoryRequestDTO(requestDTO.userId(), requestDTO.productId(), requestDTO.orderId(), requestDTO.quantity());
    }

    private PaymentRequestDTO getPaymentRequestDTO(OrchestratorRequestDTO requestDTO) {
        return new PaymentRequestDTO(requestDTO.userId(), requestDTO.orderId(), requestDTO.amount(), requestDTO.quantity());
    }

}

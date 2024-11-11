package com.sree.orchestrator.service;

import com.sree.orchestrator.common.PaymentRequestDTO;
import com.sree.orchestrator.common.PaymentResponseDTO;
import com.sree.orchestrator.common.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PaymentStep implements WorkflowStep{

    private final WebClient webClient;
    private final PaymentRequestDTO paymentRequestDTO;
    private WorkflowStepStatus workflowStepStatus = WorkflowStepStatus.PENDING;

    @Override
    public WorkflowStepStatus getStatus() {
        return workflowStepStatus;
    }

    @Override
    public Mono<Boolean> process() {
        return webClient
                .post()
                .uri("/payment/debit")
                .body(BodyInserters.fromValue(paymentRequestDTO))
                .retrieve()
                .bodyToMono(PaymentResponseDTO.class)
                .map(res -> res.status().equals(PaymentStatus.PAYMENT_APPROVED))
                .doOnNext(r -> workflowStepStatus = r ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> revert() {
        return webClient
                .post()
                .uri("/payment/credit")
                .body(BodyInserters.fromValue(paymentRequestDTO))
                .retrieve()
                .bodyToMono(Void.class)
                .map(res -> true)
                .onErrorReturn(false);
    }
}

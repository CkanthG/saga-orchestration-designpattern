package com.sree.order.consumer;

import com.sree.order.dto.OrchestratorRequestDTO;
import com.sree.order.dto.OrchestratorResponseDTO;
import com.sree.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {

    private final Flux<OrchestratorRequestDTO> flux;
    private final OrderService orderService;

    @Bean
    public Supplier<Flux<OrchestratorRequestDTO>> fluxSupplier() {
        return () -> flux;
    }

    @Bean
    public Consumer<Flux<OrchestratorResponseDTO>> fluxConsumer() {
        return c -> c.doOnNext(consume -> log.info("Consuming {}", consume))
                .flatMap(orderService::updateOrder)
                .subscribe();
    }

}

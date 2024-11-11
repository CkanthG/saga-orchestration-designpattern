package com.sree.orchestrator.config;

import com.sree.orchestrator.common.OrchestratorRequestDTO;
import com.sree.orchestrator.common.OrchestratorResponseDTO;
import com.sree.orchestrator.service.OrchestratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class OrchestratorConfig {

    private final OrchestratorService orchestratorService;

    @Bean
    public Function<Flux<OrchestratorRequestDTO>, Flux<OrchestratorResponseDTO>> processor() {
        return flux -> flux.flatMap(orchestratorService::orderProduct)
                .doOnNext(o -> log.info("Status : {}", o));
    }

}

package com.gordeok.chat.service;

import com.gordeok.chat.dto.FraudAnalyzeRequestDto;
import com.gordeok.chat.dto.FraudAnalyzeResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class FraudDetectionClient {

    private final WebClient webClient;

    public FraudDetectionClient(
            WebClient.Builder webClientBuilder,
            @Value("${fraud.server.url}") String fraudServerUrl
    ) {
        this.webClient = webClientBuilder
                .baseUrl(fraudServerUrl)
                .build();
    }

    public Mono<FraudAnalyzeResponseDto> analyze(FraudAnalyzeRequestDto request) {
        return webClient.post()
                .uri("/api/fraud/analyze")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(FraudAnalyzeResponseDto.class)
                .timeout(Duration.ofSeconds(35))
                .onErrorResume(e -> {
                    log.warn("사기 탐지 서버 호출 실패 (무시하고 계속): {}", e.getMessage());
                    return Mono.empty();
                });
    }
}

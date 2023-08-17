package org.gcnc.calculate.fetcher;

import lombok.Builder;
import lombok.Getter;
import org.gcnc.calculate.fetcher.config.FetcherProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class Fetcher {
    private final WebClient webClient;

    public Fetcher(WebClient.Builder webClientBuilder, FetcherProperties fetcherProperties) {
        this.webClient = webClientBuilder.baseUrl(fetcherProperties.getUrl())
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }

    public Mono<String> fetchResponse(String url) {
        return webClient.post()
                .uri("/retrieve")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(FetcherRequest.builder().url(url).build()))
                .retrieve()
                .bodyToMono(String.class).single();
    }

    @Builder
    @Getter
    public static class FetcherRequest {
        private String url;
    }
}

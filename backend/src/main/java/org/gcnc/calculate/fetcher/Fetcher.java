package org.gcnc.calculate.fetcher;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class Fetcher {
    private WebClient webClient;
    private String host = "http://localhost:5000";

    public Fetcher(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(host).build();
    }

    public Mono<String> fetchResponse(String url) {
        return webClient.post()
                .uri("/retrieve")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(new Request(url)))
                .retrieve()
                .bodyToMono(String.class).single();
    }

    @Data
    @AllArgsConstructor
    private static class Request {
        private String url;
    }
}

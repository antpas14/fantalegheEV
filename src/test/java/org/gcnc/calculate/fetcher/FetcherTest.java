package org.gcnc.calculate.fetcher;

import org.gcnc.calculate.config.FetcherProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetcherTest {
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.Builder builder;
    private Fetcher fetcher;

    @BeforeEach
    void setup() {
        FetcherProperties fetcherProperties = new FetcherProperties();
        fetcherProperties.setUrl("url");
        when(builder.baseUrl(anyString())).thenReturn(builder);
        when(builder.exchangeStrategies((ExchangeStrategies) any())).thenReturn(builder);
        when(builder.build()).thenReturn(webClient);
        fetcher = new Fetcher(builder, fetcherProperties);
    }

    @Test
    public void fetchTest() {
        WebClient.RequestBodyUriSpec requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec requestBodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.accept(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(BodyInserter.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("test"));

        StepVerifier.create(fetcher.fetchResponse("url-test"))
                .expectNextMatches(response -> response.equals("test"))
                .verifyComplete();
    }

    @Test
    public void requestTest() {
        Fetcher.FetcherRequest request = Fetcher.FetcherRequest.builder().url("url").build();
        assertEquals("url", request.getUrl());
    }

}
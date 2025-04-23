package org.gcnc.calculate.controller;

import org.gcnc.calculate.service.CalculateService;
import org.gcnc.fantalegheev_api.model.Rank;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculateControllerTest {
    @InjectMocks
    private CalculateController calculateController;

    @Mock
    private CalculateService calculateService;

    @Mock
    private Flux<Part> mockFilePart;

    @Test
    void calculatePostReturnsOk() throws Exception {
        // Arrange
        when(calculateService.calculateResponse(mockFilePart)).thenReturn(Flux.empty());

        // Act
        Mono<ResponseEntity<Flux<Rank>>> responseMono = calculateController.calculate(mockFilePart);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                })
                .verifyComplete();
    }
}
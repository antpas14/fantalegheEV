package org.gcnc.calculate.controller;

import org.gcnc.calculate.service.CalculateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculateControllerTest {
    @InjectMocks
    private CalculateController calculateController;

    @Mock
    private CalculateService calculateService;

    @Test
    void calculateGetTest() {
        when(calculateService.calculateResponse(any())).thenReturn(Mono.empty());

        StepVerifier.create(calculateController.calculateGet("league"))
                .assertNext(result -> assertEquals(HttpStatus.OK, result.getStatusCode()))
                .verifyComplete();
    }
}
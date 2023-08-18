package org.gcnc.calculate.service;

import org.gcnc.calculate.parser.ParserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ParserImplTest {
    private ParserImpl parser;
    @BeforeEach
    public void init() {
        parser = new ParserImpl();
    }

    @Test
    public void getResultsTest() throws IOException {
        // Given
        File calendarFile = new File("src/test/resources/html/calendar.txt");
        Mono<String> calendar = Mono.just(new String(Files.readAllBytes(Paths.get(calendarFile.getAbsolutePath()))));

        // When / Then
        StepVerifier.create(parser.getResults(calendar))
                .assertNext(response -> {
                    assertEquals("A", response.get(1).get(0).team());
                    assertEquals(3, response.get(1).get(0).goal());
                })
                .verifyComplete();
         }

    @Test
    public void getPointsTest() throws IOException {
        File rankingFile = new File("src/test/resources/html/ranking.txt");
        Mono<String> rankingPage = Mono.just(new String(Files.readAllBytes(Paths.get(rankingFile.getAbsolutePath()))));

        StepVerifier.create(parser.getPoints(rankingPage))
                .assertNext(response -> {
                    assertEquals(17, response.get("A"));
                    assertEquals(11, response.get("E"));
                })
                .verifyComplete();

    }
}
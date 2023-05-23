package org.gcnc.calculate.service;

import org.gcnc.calculate.config.CalculateProperties;
import org.gcnc.calculate.fetcher.Fetcher;
import org.gcnc.calculate.model.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParserImplTest {
    @Mock
    private Fetcher fetcher;
    private CalculateProperties calculateProperties;
    private ParserImpl parser;
    @BeforeEach
    public void init() {
        initializeProperties();
        parser = new ParserImpl(calculateProperties, fetcher);
    }

    @Test
    public void getResultsTest() throws IOException {
        // Given
        File calendarFile = new File("src/test/resources/html/calendar.txt");
        String calendar = new String(Files.readAllBytes(Paths.get(calendarFile.getAbsolutePath())));
        when(fetcher.fetchResponse(any()))
                .thenReturn(Mono.just(calendar));

        Request request = new Request("league-name");
        // When / Then
        StepVerifier.create(parser.getResults(request.leagueName()))
                .assertNext(response -> {
                    assertEquals("A", response.get(1).get(0).team());
                    assertEquals(3, response.get(1).get(0).goal());
                })
                .verifyComplete();
         }

    @Test
    public void getPointsTest() throws IOException {
        File rankingFile = new File("src/test/resources/html/ranking.txt");
        String ranking = new String(Files.readAllBytes(Paths.get(rankingFile.getAbsolutePath())));
        when(fetcher.fetchResponse(any()))
                .thenReturn(Mono.just(ranking));
        Request request = new Request("league-name");

        StepVerifier.create(parser.getPoints(request.leagueName()))
                .assertNext(response -> {
                    assertEquals(17, response.get("A"));
                    assertEquals(11, response.get("E"));
                })
                .verifyComplete();

    }

    private void initializeProperties() {
        calculateProperties = new CalculateProperties();
        calculateProperties.setBaseUrl("baseurl");
    }
}
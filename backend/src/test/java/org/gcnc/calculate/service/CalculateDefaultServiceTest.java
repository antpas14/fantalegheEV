package org.gcnc.calculate.service;

import org.gcnc.calculate.fetcher.Fetcher;
import org.gcnc.calculate.config.CalculateProperties;
import org.gcnc.calculate.model.Request;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringJUnit4ClassRunner.class)
public class CalculateDefaultServiceTest {
    @Mock
    private Fetcher fetcher;
    @Mock
    private CalculateProperties calculateProperties;
    @InjectMocks
    private CalculateDefaultService calculateService;
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        initializeProperties();
        calculateService = new CalculateDefaultService(calculateProperties, fetcher);
    }

    @Test
    public void calculateTest() throws IOException {
        // Given
        File rankingFile = new File("src/test/resources/html/ranking.html");
        File calendarFile = new File("src/test/resources/html/calendar.html");

        String ranking = new String(Files.readAllBytes(Paths.get(rankingFile.getAbsolutePath())));
        String calendar = new String(Files.readAllBytes(Paths.get(calendarFile.getAbsolutePath())));
        Mockito.when(fetcher.fetchResponse(any()))
                .thenReturn(Mono.just(calendar))
                .thenReturn(Mono.just(ranking));

        Request request = new Request("league-name");
        // When
        // List<Rank> response = calculateService.calculateResponse(request).block();
        StepVerifier.create(calculateService.calculateResponse(request))
                .expectNextMatches(response -> {
                    Assert.assertEquals("Uniao Sao Joao", response.get(0).getTeam());
                    Assert.assertEquals(1, response.get(0).getPoints());
                    return true;
                })
                .expectComplete();


        // Then
        //Assert.assertEquals("Uniao Sao Joao", response.get(0).getTeam());
        //Assert.assertEquals(new Double(2.7142857142857144), response.get(0).getEvPoints());
    }

    private void initializeProperties() {
        calculateProperties.setBaseUrl("http://baseurl");
        calculateProperties.setCalendarSuffix("/calendar");
    }
}
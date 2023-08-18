package org.gcnc.calculate.service;

import org.gcnc.calculate.config.CalculateProperties;
import org.gcnc.calculate.fetcher.Fetcher;
import org.gcnc.calculate.model.Request;
import org.gcnc.calculate.model.TeamResult;
import org.gcnc.calculate.parser.Parser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculateServiceImplTest {
    @InjectMocks
    private CalculateServiceImpl calculateService;
    @Mock
    private CalculateProperties properties;
    @Mock
    private Parser parser;
    @Mock
    private Fetcher fetcher;

    @Test
    public void fetchRanking() {
        // Given
        Map<Integer, List<TeamResult>> results = Map.of(
                1, List.of(new TeamResult("A", 3),
                        new TeamResult("B", 2),
                        new TeamResult("C", 3),
                        new TeamResult("D", 1)),
                2, List.of(new TeamResult("A", null),
                        new TeamResult("B", null),
                        new TeamResult("C", null),
                        new TeamResult("D", null))
            );
        Map<String, Integer> rankings = Map.of("A", 1, "B", 3, "C", 1, "D", 0);
        when(properties.getBaseUrl()).thenReturn("url");
        when(properties.getRankingSuffix()).thenReturn("ranking");
        when(properties.getCalendarSuffix()).thenReturn("calendar");
        when(fetcher.fetchResponse(anyString())).thenReturn(Mono.just("response"));
        when(parser.getResults(any())).thenReturn(Mono.just(results));
        when(parser.getPoints(any())).thenReturn(Mono.just(rankings));
        Request request = new Request("league-name");

        // When/Then
        StepVerifier.create(calculateService.calculateResponse(request))
                .assertNext(teamRank -> {
                    assertEquals("B", teamRank.getTeam());
                    assertEquals(Double.parseDouble("1.0"), teamRank.getEvPoints());
                })
                .assertNext(teamRank -> {
                    assertEquals("A", teamRank.getTeam());
                    assertEquals(Double.parseDouble("2.3333333333333335"), teamRank.getEvPoints());
                })
                .assertNext(teamRank -> {
                    assertEquals("C", teamRank.getTeam());
                    assertEquals(Double.parseDouble("2.3333333333333335"), teamRank.getEvPoints());
                })
                .assertNext(teamRank -> {
                    assertEquals("D", teamRank.getTeam());
                    assertEquals(Double.parseDouble("0"), teamRank.getEvPoints());
                })
                .verifyComplete();
    }

}
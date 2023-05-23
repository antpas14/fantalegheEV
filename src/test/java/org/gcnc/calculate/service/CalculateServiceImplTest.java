package org.gcnc.calculate.service;

import org.gcnc.calculate.model.Rank;
import org.gcnc.calculate.model.Request;
import org.gcnc.calculate.model.TeamResult;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculateServiceImplTest {
    @InjectMocks
    private CalculateServiceImpl calculateService;
    @Mock
    private ParserImpl parser = mock(ParserImpl.class);

    @Test
    public void fetchRanking() {
        // Given
        Map<Integer, List<TeamResult>> results = Map.of(1,
                List.of(new TeamResult("A", 3),
                        new TeamResult("B", 2),
                        new TeamResult("C", 3),
                        new TeamResult("D", 1)));
        Map<String, Integer> rankings = Map.of("A", 1, "B", 3, "C", 1, "D", 0);
        when(parser.getResults(anyString())).thenReturn(Mono.just(results));
        when(parser.getPoints(anyString())).thenReturn(Mono.just(rankings));
        Request request = new Request("league-name");

        // When/Then
        StepVerifier.create(calculateService.calculateResponse(request))
                .assertNext(response -> {
                    Rank teamRank = response.stream()
                            .filter(teamRanking -> "B".equals(teamRanking.team()))
                            .findFirst()
                            .get();
                    assertEquals("B", teamRank.team());
                    assertEquals(Double.parseDouble("1.0"), teamRank.evPoints());
                })
                .verifyComplete();
    }

}
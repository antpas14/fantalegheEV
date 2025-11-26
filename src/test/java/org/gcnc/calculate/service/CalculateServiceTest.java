package org.gcnc.calculate.service;

import org.gcnc.calculate.excel.ExcelService;
import org.gcnc.calculate.model.MatchResults;
import org.gcnc.calculate.model.TeamResult;
import org.gcnc.calculate.parser.Parser;
import org.gcnc.fantalegheev_api.model.Rank;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withPrecision;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculateServiceTest {
    @Mock
    private ExcelService excelService;

    @Mock
    private Parser parser;

    @InjectMocks
    private CalculateService calculateService;

    @Test
    void calculateResponseWithValidResults() {
        // Given
        Part mockPart = mock(Part.class);
        List<MatchResults> matchResults = List.of(
            new MatchResults(List.of(
                new TeamResult("Team A", 3, 3),
                new TeamResult("Team B", 1, 0),
                new TeamResult("Team C", 2, 1),
                new TeamResult("Team D", 2, 1)
            )),
            new MatchResults(List.of(
                new TeamResult("Team A", 2, 3),
                new TeamResult("Team B", 2, 1),
                new TeamResult("Team C", 1, 0),
                new TeamResult("Team D", 3, 3)
            ))
        );

        when(excelService.readExcel(any())).thenReturn(Mono.just(List.of(
            List.of("Team A", "3", "Team B", "1", "Team C", "2", "Team D", "2"),
            List.of("Team A", "2", "Team B", "2", "Team C", "1", "Team D", "3")
        )));
        when(parser.getTeamResults(any())).thenReturn(matchResults);

        // Expected results for each team with per-match points divided by number of opponents (3)
        Map<String, RankExpectation> expectedResults = Map.of(
            "Team D", new RankExpectation(4, 4.33),  // Match 1: (4/3=1.33) + Match 2: (9/3=3.0) = 4.33
            "Team A", new RankExpectation(6, 4.33),  // Match 1: (9/3=3.0) + Match 2: (4/3=1.33) = 4.33
            "Team B", new RankExpectation(1, 1.33),  // Match 1: (0/3=0.0) + Match 2: (4/3=1.33) = 1.33
            "Team C", new RankExpectation(1, 1.33)   // Match 1: (4/3=1.33) + Match 2: (0/3=0.0) = 1.33
        );

        // When
        Flux<Rank> result = calculateService.calculateResponse(mockPart);

        // Then
        StepVerifier.create(result)
            .expectNextCount(4)
            .thenConsumeWhile(
                rank -> {
                    var expected = expectedResults.get(rank.getTeam());
                    assertThat(rank.getPoints()).isEqualTo(expected.points());
                    assertThat(rank.getEvPoints()).isCloseTo(expected.evPoints(), withPrecision(0.01));
                    return true;
                }
            )
            .verifyComplete();
    }

    @Test
    void calculateResponseWithNullGoals() {
        // Given
        Part mockPart = mock(Part.class);
        List<MatchResults> matchResults = List.of(
            new MatchResults(List.of(
                new TeamResult("Team A", null, null),
                new TeamResult("Team B", null, null),
                new TeamResult("Team C", null, null),
                new TeamResult("Team D", null, null)
            ))
        );

        when(excelService.readExcel(any())).thenReturn(Mono.just(List.of(
            List.of("Team A", "", "Team B", "", "Team C", "", "Team D", "")
        )));
        when(parser.getTeamResults(any())).thenReturn(matchResults);

        // When
        Flux<Rank> result = calculateService.calculateResponse(mockPart);

        // Then
        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void calculateResponseWithEmptyResults() {
        // Given
        Part mockPart = mock(Part.class);
        when(excelService.readExcel(any())).thenReturn(Mono.just(List.of()));
        when(parser.getTeamResults(any())).thenReturn(List.of());

        // When
        Flux<Rank> result = calculateService.calculateResponse(mockPart);

        // Then
        StepVerifier.create(result)
            .verifyComplete();
    }

    @Test
    void calculateResponseWithError() {
        // Given
        Part mockPart = mock(Part.class);
        when(excelService.readExcel(any())).thenReturn(Mono.error(new RuntimeException("Test error")));

        // When
        Flux<Rank> result = calculateService.calculateResponse(mockPart);

        // Then
        StepVerifier.create(result)
            .expectError(RuntimeException.class)
            .verify();
    }

    private record RankExpectation(int points, double evPoints) {}
}

package org.gcnc.calculate.parser;

import org.gcnc.calculate.model.MatchResults;
import org.gcnc.calculate.model.TeamResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private final Parser parser = new Parser();

    @Test
    void getTeamResults_emptyCalendar_returnsEmptyList() {
        List<List<String>> calendar = new ArrayList<>();
        List<MatchResults> results = parser.getTeamResults(calendar);
        assertTrue(results.isEmpty());
    }

    @Test
    void getTeamResults_singleMatchdaySingleMatch_returnsSingleMatchResult() {
        List<List<String>> calendar = List.of(
                List.of("Giornata 1", "", "", "", "", "", "", "", "", ""),
                List.of("Team A", "", "", "Team B", "2-1", "", "", "", "", "")
        );
        List<MatchResults> results = parser.getTeamResults(calendar);
        assertEquals(1, results.size());
        MatchResults matchResults = results.getFirst();
        assertEquals(2, matchResults.results().size());

        TeamResult teamAResult = matchResults.results().stream().filter(tr -> tr.team().equals("Team A")).findFirst().orElseThrow();
        assertEquals(2, teamAResult.goal());
        assertEquals(3, teamAResult.points());

        TeamResult teamBResult = matchResults.results().stream().filter(tr -> tr.team().equals("Team B")).findFirst().orElseThrow();
        assertEquals(1, teamBResult.goal());
        assertEquals(0, teamBResult.points());
    }

    @Test
    void getTeamResults_singleMatchdayMultipleMatches_returnsSingleMatchResultWithMultipleTeamResults() {
        List<List<String>> calendar = List.of(
                List.of("Giornata 1", "", "", "", "", "", "", "", "", ""),
                List.of("Team A", "", "", "Team B", "2-1", "", "", "", "", ""),
                List.of("Team C", "", "", "Team D", "0-0", "", "", "", "", "")
        );
        List<MatchResults> results = parser.getTeamResults(calendar);
        assertEquals(1, results.size());
        MatchResults matchResults = results.getFirst();
        assertEquals(4, matchResults.results().size());

        TeamResult teamAResult = matchResults.results().stream().filter(tr -> tr.team().equals("Team A")).findFirst().orElseThrow();
        assertEquals(2, teamAResult.goal());
        assertEquals(3, teamAResult.points());

        TeamResult teamBResult = matchResults.results().stream().filter(tr -> tr.team().equals("Team B")).findFirst().orElseThrow();
        assertEquals(1, teamBResult.goal());
        assertEquals(0, teamBResult.points());

        TeamResult teamCResult = matchResults.results().stream().filter(tr -> tr.team().equals("Team C")).findFirst().orElseThrow();
        assertEquals(0, teamCResult.goal());
        assertEquals(1, teamCResult.points());

        TeamResult teamDResult = matchResults.results().stream().filter(tr -> tr.team().equals("Team D")).findFirst().orElseThrow();
        assertEquals(0, teamDResult.goal());
        assertEquals(1, teamDResult.points());
    }

    @Test
    void getTeamResults_multipleMatchdaysMultipleMatches_returnsMultipleMatchResults() {
        List<List<String>> calendar = List.of(
                List.of("Giornata 1", "", "", "", "", "", "", "", "", ""),
                List.of("Team A", "", "", "Team B", "2-1", "", "", "", "", ""),
                List.of("Team C", "", "", "Team D", "0-0", "", "", "", "", ""),
                List.of("Giornata 2", "", "", "", "", "", "", "", "", ""),
                List.of("Team E", "", "", "Team F", "1-3", "", "", "", "", "")
        );
        List<MatchResults> results = parser.getTeamResults(calendar);
        assertEquals(2, results.size());

        MatchResults matchday1Results = results.getFirst();
        assertEquals(4, matchday1Results.results().size());
        assertTrue(matchday1Results.results().stream().anyMatch(tr -> tr.team().equals("Team A") && tr.points() == 3));
        assertTrue(matchday1Results.results().stream().anyMatch(tr -> tr.team().equals("Team B") && tr.points() == 0));
        assertTrue(matchday1Results.results().stream().anyMatch(tr -> tr.team().equals("Team C") && tr.points() == 1));
        assertTrue(matchday1Results.results().stream().anyMatch(tr -> tr.team().equals("Team D") && tr.points() == 1));

        MatchResults matchday2Results = results.get(1);
        assertEquals(2, matchday2Results.results().size());
        assertTrue(matchday2Results.results().stream().anyMatch(tr -> tr.team().equals("Team E") && tr.points() == 0));
        assertTrue(matchday2Results.results().stream().anyMatch(tr -> tr.team().equals("Team F") && tr.points() == 3));
    }

    @Test
    void getTeamResults_matchWithNoScore_goalsAndPointsAreNullOrZero() {
        List<List<String>> calendar = List.of(
                List.of("Giornata 1", "", "", "", "", "", "", "", "", ""),
                List.of("Team A", "", "", "Team B", "-", "", "", "", "", "")
        );
        List<MatchResults> results = parser.getTeamResults(calendar);
        assertEquals(0, results.size());
        }

    @Test
    void getTeamResults_calendarWithMatchdayOnly_returnsEmptyList() {
        List<List<String>> calendar = List.of(
                List.of("Giornata 1", "", "", "", "", "", "", "", "", "")
        );
        List<MatchResults> results = parser.getTeamResults(calendar);
        assertTrue(results.isEmpty());
    }

    @Test
    void splitRows_validRow_splitsCorrectly() {
        List<List<String>> rows = List.of(
                List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")
        );
        List<List<String>> split = parser.splitRows(rows);
        assertEquals(2, split.size());
        assertEquals(List.of("A", "B", "C", "D", "E"), split.getFirst());
        assertEquals(List.of("F", "G", "H", "I", "J"), split.get(1));
    }

    @Test
    void splitRows_rowWithInvalidSize_isIgnored() {
        List<List<String>> rows = List.of(
                List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"),
                List.of("A", "B", "C")
        );
        List<List<String>> split = parser.splitRows(rows);
        assertEquals(2, split.size());
        assertEquals(List.of("A", "B", "C", "D", "E"), split.getFirst());
        assertEquals(List.of("F", "G", "H", "I", "J"), split.get(1));
    }

    @Test
    void splitRows_emptyList_returnsEmptyList() {
        List<List<String>> rows = new ArrayList<>();
        List<List<String>> split = parser.splitRows(rows);
        assertTrue(split.isEmpty());
    }
}
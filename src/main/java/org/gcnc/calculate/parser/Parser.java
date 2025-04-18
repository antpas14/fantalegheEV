package org.gcnc.calculate.parser;

import org.gcnc.calculate.model.MatchResults;
import org.gcnc.calculate.model.TeamResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.gcnc.calculate.util.MatchUtils.calculateMatchPoints;

@Service
public class Parser {
    public List<MatchResults> getTeamResults(List<List<String>> calendar) {
        List<MatchResults> results = new ArrayList<>();
        List<TeamResult> teamResults = new ArrayList<>();
        for (List<String> calendarRow : splitRows(calendar)) {
            // First row is "Giornata X". Exclude that line. Parse a List till we get next Giornata
            if (calendarRow.getFirst().contains("Giornata")) {
                if (!teamResults.isEmpty()) {
                    results.add(new MatchResults(teamResults));
                }
                teamResults = new ArrayList<>();
                continue;
            }
            teamResults.addAll(getTeamResult(calendarRow));
        }
        if (!teamResults.isEmpty()) {
            results.add(new MatchResults(teamResults)); // Add last match day
        }
        return results;
    }

    private List<TeamResult> getTeamResult(List<String> match) {
        String[] goals = match.get(4).split("-");
        if (!(goals.length == 2)) {
            return Collections.emptyList();
        }

        // This will return 2 team results, for the 2 teams involved in the matchF
        String teamA = match.get(0);
        String teamB = match.get(3);

        int goalA = Integer.parseInt(goals[0]);
        int goalB = Integer.parseInt(goals[1]);

        // Evaluate result
        return List.of(new TeamResult(teamA, goalA, calculateMatchPoints(goalA, goalB)),
                new TeamResult(teamB, goalB, calculateMatchPoints(goalB, goalA)));
    }

    List<List<String>> splitRows(List<List<String>> rows) {
        List<List<String>> firstHalves = new ArrayList<>();
        List<List<String>> secondHalves = new ArrayList<>();
        List<List<String>> result = new ArrayList<>();

        for (List<String> innerList : rows) {
            if (innerList.size() != 10) {
                continue;
            }
            int midpoint = rows.getFirst().size() / 2;
            List<String> firstHalf = innerList.stream().limit(midpoint).collect(Collectors.toList());
            List<String> secondHalf = innerList.stream().skip(midpoint).collect(Collectors.toList());
            firstHalves.add(firstHalf);
            secondHalves.add(secondHalf);
        }

        result.addAll(firstHalves);
        result.addAll(secondHalves);

        return result;
    }
}

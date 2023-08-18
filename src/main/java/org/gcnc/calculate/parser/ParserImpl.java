package org.gcnc.calculate.parser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gcnc.calculate.model.TeamResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class ParserImpl implements Parser {

    @Override
    public Mono<Map<String, Integer>> getPoints(Mono<String> rankingsPage) {
        return rankingsPage
                .map(Jsoup::parse)
                .map(doc -> getRankingTable(doc).stream()
                        .collect(Collectors.toMap(this::getTeamNameFromRankingTable, this::getTeamPointsFromRankingTable)));
    }

    @Override
    public Mono<Map<Integer, List<TeamResult>>> getResults(Mono<String> calendarPage) {
        return calendarPage
                .map(Jsoup::parse)
                .map(this::selectCalendarDaysFromCalendarDocument)
                .map(this::createResultsMap);
    }

    private Map<Integer, List<TeamResult>> createResultsMap(Elements calendarDays) {
        AtomicInteger counter = new AtomicInteger();
        return calendarDays.stream()
                .map(this::getMatchesFromCalendarDay)
                .map(match -> getTeamsFromMatches(match)
                        .stream()
                        .map(team -> new TeamResult(getTeamNameFromMatch(team), getTeamPointsFromMatch(team)))
                        .collect(Collectors.toList()))
                .collect(Collectors.toMap(m-> counter.incrementAndGet(), m -> m));
    }

    // Utils methods
    private Elements selectCalendarDaysFromCalendarDocument(Document calendar) {
        return calendar.select(".match-frame");
    }

    private Elements getMatchesFromCalendarDay(Element calendarDay) {
        return calendarDay.select(".match");
    }

    private Elements getTeamsFromMatches(Elements matches) {
        return matches.select(".team");
    }

    private String getTeamNameFromMatch(Element t) {
        return t.select(".team-name").get(0).text();
    }

    private Integer getTeamPointsFromMatch(Element t) {
        if (Double.parseDouble(t.select(".team-fpt").text()) > 0.0) {
            return Integer.parseInt(t.select(".team-score").get(0).text());
        } else {
            return null;
        }
    }
    private String getTeamNameFromRankingTable(Element e) {
        return e.children().get(2).children().get(0).children().get(0).html();
    }

    private Integer getTeamPointsFromRankingTable(Element e) {
        return Integer.parseInt(e.children().get(10).children().get(0).html());
    }

    private Elements getRankingTable(Document doc) {
        return doc.select(".ranking").get(0).children().get(0).children().get(1).children();
    }
}

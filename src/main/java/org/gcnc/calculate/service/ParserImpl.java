package org.gcnc.calculate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gcnc.calculate.config.CalculateProperties;
import org.gcnc.calculate.fetcher.Fetcher;
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
    private final CalculateProperties calculateProperties;
    private final Fetcher fetcher;

    @Override
    public Mono<Map<Integer, List<TeamResult>>> getResults(String leagueName) {
        String url = calculateProperties.getBaseUrl() + leagueName + calculateProperties.getCalendarSuffix();
        return fetcher.fetchResponse(url)
                .map(Jsoup::parse)
                .map(doc -> doc.select(".match-frame"))
                .map(this::createResultsMap);
    }

    public Mono<Map<String, Integer>> getPoints(String leagueName) {
        return fetcher.fetchResponse(calculateProperties.getBaseUrl() + leagueName + calculateProperties.getRankingSuffix())
                .map(Jsoup::parse)
                .map(doc -> getRankingTable(doc).stream()
                        .collect(Collectors.toMap(this::getTeamNameFromRankingTable, this::getTeamPointsFromRankingTable)));
    }

    private Map<Integer, List<TeamResult>> createResultsMap(Elements calendarDays) {
        AtomicInteger counter = new AtomicInteger();
        return calendarDays.stream()
                .map(calendarDay -> calendarDay.select(".match"))
                .map(match -> match.select(".team").stream()
                        .map(team -> new TeamResult(getTeamNameFromMatch(team), getTeamPointsFromMatch(team)))
                        .collect(Collectors.toList()))
                .collect(Collectors.toMap(m-> counter.incrementAndGet(), m -> m));
    }

    // Utils methods
    private String getTeamNameFromMatch(Element t) {
        return t.select(".team-name").get(0).text();
    }

    private Integer getTeamPointsFromMatch(Element t) {
        return Integer.parseInt(t.select(".team-score").get(0).text());
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

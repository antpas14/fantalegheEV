package org.gcnc.calculate.service;

import org.gcnc.calculate.fetcher.Fetcher;
import org.gcnc.calculate.config.CalculateProperties;
import org.gcnc.calculate.model.Rank;
import org.gcnc.calculate.model.Request;
import org.gcnc.calculate.model.TeamResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class CalculateDefaultService implements CalculateService {

    private CalculateProperties calculateProperties;
    private Fetcher fetcher;

    @Autowired
    CalculateDefaultService(CalculateProperties calculateProperties, Fetcher fetcher) {
        this.fetcher = fetcher;
        this.calculateProperties = calculateProperties;
    }

    private final Logger logger = LoggerFactory.getLogger(CalculateDefaultService.class);

    @Override
    public Mono<List<Rank>> calculateResponse(Request req) {
        logger.info("New request for league {}", req.getLeagueName());
        return Mono.zip(getResults(req.getLeagueName()), getPoints(req.getLeagueName()))
                .map(resultsAndPointsTuple -> getEVRanking(calculateEVRank(resultsAndPointsTuple.getT1()), resultsAndPointsTuple.getT2()));
    }

    private List<Rank> getEVRanking(final Map<String, Double> evPoints, final Map<String, Integer> points) {
        return evPoints.keySet().stream()
                .map(t -> new Rank(t, evPoints.get(t), points.get(t)))
                .sorted((r1, r2) -> r2.getPoints() - r1.getPoints())
                .collect(Collectors.toList());
    }

    private Map<String, Double> calculateEVRank(Map<Integer, List<TeamResult>> results) {
        final Map<String, Double> evRank = new HashMap<>();
        results.get(1)
                .forEach(tr -> evRank.put(tr.getTeam(), 0D));
        final int combinations = results.get(1).size() - 1;
        results.forEach((k, v) -> {
            for (int i = 0; i < v.size(); i++) {
                TeamResult t1 = v.get(i);
                double points = 0D;
                for (int j = 0; j < v.size(); j++) {
                    if (i != j) {
                        TeamResult t2 = v.get(j);
                        points += calculateMatchPoints(t1, t2);
                    }
                }
                evRank.put(t1.getTeam(), (points / combinations) + evRank.get(t1.getTeam()));
            }
        });
        return evRank;
    }

    private Double calculateMatchPoints(TeamResult t1, TeamResult t2){
        if (t1.getGoal() > t2.getGoal()) {
            return 3D;
        } else if (t1.getGoal() == t2.getGoal()) {
            return 1D;
        } else {
            return 0D;
        }
    }

    private Mono<Map<Integer, List<TeamResult>>> getResults(String leagueName) {
        String url = calculateProperties.getBaseUrl() + leagueName + calculateProperties.getCalendarSuffix();
        return fetcher.fetchResponse(url)
                .map(Jsoup::parse)
                .map(doc -> doc.select(".calendar"))
                .map(doc -> doc.select(".match-frame"))
                .map(this::createMap);
    }

    private Map<Integer, List<TeamResult>> createMap(Elements calendarDays) {
        AtomicInteger counter = new AtomicInteger();
        return calendarDays.stream()
            .map(c-> c.select(".match"))
            .map(m -> m.select(".team").stream()
                    .filter(this::isValidTeamName)
                    .filter(this::isValidResult)
                    .map(t -> new TeamResult(getTeamNameFromMatch(t), getTeamPointsFromMatch(t)))
                    .collect(Collectors.toList()))
            .filter(l -> !l.isEmpty())
            .collect(Collectors.toMap(m-> counter.incrementAndGet(), m -> m));
    }

    private Mono<Map<String, Integer>> getPoints(String leagueName) {
        return fetcher.fetchResponse(calculateProperties.getBaseUrl() + leagueName + calculateProperties.getRankingSuffix())
                .map(Jsoup::parse)
                .map(doc -> getRankingTable(doc).stream()
                        .collect(Collectors.toMap(this::getTeamNameFromRankingTable, this::getTeamPointsFromRankingTable)
                ));
    }

    private boolean isValidTeamName(Element t) {
        return t.select(".team-score").size() > 0 && !t.select(".team-score").get(0).text().equals("");
    }

    private String getTeamNameFromMatch(Element t) {
        return t.select(".team-name").get(0).text();
    }

    private Integer getTeamPointsFromMatch(Element t) {
        return Integer.parseInt(t.select(".team-score").get(0).text());
    }

    private boolean isValidResult(Element t) {
        return Double.parseDouble(t.select(".team-fpt").text()) > 0.0 && !t.select(".team-fpt").get(0).text().equals("");
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

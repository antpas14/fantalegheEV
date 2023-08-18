package org.gcnc.calculate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gcnc.calculate.config.CalculateProperties;
import org.gcnc.calculate.fetcher.Fetcher;
import org.gcnc.calculate.model.Request;
import org.gcnc.calculate.model.TeamResult;
import org.gcnc.calculate.parser.Parser;
import org.gcnc.fantalegheev_api.model.Rank;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class CalculateServiceImpl implements CalculateService {
    private final Parser parser;
    private final Fetcher fetcher;
    private final CalculateProperties calculateProperties;
    @Override
    public Flux<Rank> calculateResponse(Request req) {
        log.info("New request for league {}", req.leagueName());
        return Flux.zip(parser.getResults(getCalendarPage(req.leagueName())), parser.getPoints(getRankingPage(req.leagueName())))
                .flatMap(resultsAndPointsTuple -> getEVRanking(calculateEVRank(resultsAndPointsTuple.getT1()), resultsAndPointsTuple.getT2()));
    }

    private Mono<String> getRankingPage(final String leagueName) {
        return fetcher.fetchResponse(calculateProperties.getBaseUrl() + leagueName + calculateProperties.getRankingSuffix());
    }

    private Mono<String> getCalendarPage(final String leagueName) {
        return fetcher.fetchResponse(calculateProperties.getBaseUrl() + leagueName + calculateProperties.getCalendarSuffix());
    }

    private Flux<Rank> getEVRanking(final Map<String, Double> evPoints, final Map<String, Integer> points) {
        return Flux.fromStream(evPoints.keySet().stream()
                .map(team -> Rank.builder()
                        .team(team)
                        .evPoints(evPoints.get(team))
                        .points(points.get(team)).build())
                .sorted((r1, r2) -> r2.getPoints() - r1.getPoints()));
    }

    private Map<String, Double> calculateEVRank(Map<Integer, List<TeamResult>> results) {
        final Map<String, Double> evRank = new HashMap<>();
        // Map starts from day 1
        results.get(1)
                .forEach(tr -> evRank.put(tr.team(), 0D));
        final int combinations = results.get(1).size() - 1;
        results.forEach((k, v) -> {
            if (v.get(0).goal() == null) {
               // game has not been played yet, so we end the loop early
               return;
            }
            for (int i = 0; i < v.size(); i++) {
                TeamResult t1 = v.get(i);
                double points = 0D;
                for (int j = 0; j < v.size(); j++) {
                    if (i != j) {
                        TeamResult t2 = v.get(j);
                        points += calculateMatchPoints(t1, t2);
                    }
                }
                evRank.put(t1.team(), (points / combinations) + evRank.get(t1.team()));
            }
        });
        return evRank;
    }

    private Double calculateMatchPoints(TeamResult t1, TeamResult t2){
        if (t1.goal() > t2.goal()) {
            return 3D;
        } else if (t1.goal().equals(t2.goal())) {
            return 1D;
        } else {
            return 0D;
        }
    }
}

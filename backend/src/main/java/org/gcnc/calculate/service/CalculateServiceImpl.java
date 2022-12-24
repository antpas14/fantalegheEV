package org.gcnc.calculate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gcnc.calculate.model.Rank;
import org.gcnc.calculate.model.Request;
import org.gcnc.calculate.model.TeamResult;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class CalculateServiceImpl implements CalculateService {
    private final Parser parser;
    @Override
    public Mono<List<Rank>> calculateResponse(Request req) {
        log.info("New request for league {}", req.leagueName());
        return Mono.zip(parser.getResults(req.leagueName()), parser.getPoints(req.leagueName()))
                .map(resultsAndPointsTuple -> getEVRanking(calculateEVRank(resultsAndPointsTuple.getT1()), resultsAndPointsTuple.getT2()));
    }

    private List<Rank> getEVRanking(final Map<String, Double> evPoints, final Map<String, Integer> points) {
        return evPoints.keySet().stream()
                .map(t -> new Rank(t, evPoints.get(t), points.get(t)))
                .sorted((r1, r2) -> r2.points() - r1.points())
                .collect(Collectors.toList());
    }

    private Map<String, Double> calculateEVRank(Map<Integer, List<TeamResult>> results) {
        final Map<String, Double> evRank = new HashMap<>();
        // Map starts from day 1
        results.get(1)
                .forEach(tr -> evRank.put(tr.team(), 0D));
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
                evRank.put(t1.team(), (points / combinations) + evRank.get(t1.team()));
            }
        });
        return evRank;
    }

    private Double calculateMatchPoints(TeamResult t1, TeamResult t2){
        if (t1.goal() > t2.goal()) {
            return 3D;
        } else if (t1.goal() == t2.goal()) {
            return 1D;
        } else {
            return 0D;
        }
    }
}

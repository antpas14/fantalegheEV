package org.gcnc.calculate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.gcnc.calculate.excel.ExcelService;
import org.gcnc.calculate.model.MatchResults;
import org.gcnc.calculate.model.TeamResult;
import org.gcnc.calculate.parser.Parser;
import org.gcnc.fantalegheev_api.model.Rank;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.gcnc.calculate.util.MatchUtils.calculateMatchPoints;

@Component
@AllArgsConstructor
@Slf4j
public class CalculateService {
    private final ExcelService excelService;
    private final Parser parser;

    public Flux<Rank> calculateResponse(Flux<Part> file) throws Exception {
        return file.flatMap(excelService::readExcel)
                .map(parser::getTeamResults)
                .flatMapIterable(this::calculateEVRank);
    }

    private List<Rank> calculateEVRank(List<MatchResults> results) {
        final Map<String, Pair<Double, Integer>> evRank = new HashMap<>();

        results.stream()
            .filter(matchResult -> matchResult.results().getFirst().goal() != null)
            .forEach((matchResult) -> {
                for (int i = 0; i < matchResult.results().size(); i++) {
                    TeamResult t1 = matchResult.results().get(i);
                    double points = 0D;
                    for (int j = 0; j < matchResult.results().size(); j++) {
                        if (i != j) {
                            TeamResult t2 = matchResult.results().get(j);
                            points += calculateMatchPoints(t1.goal(), t2.goal());
                        }
                    }
                    evRank.put(t1.team(), Pair.of(
                            (points / matchResult.results().size()) + evRank.getOrDefault(t1.team(), Pair.of(0D, 0)).getLeft(),
                            t1.points() +  evRank.getOrDefault(t1.team(), Pair.of(0D, 0)).getRight()));
                }
            });

        return evRank.entrySet().stream()
                .map(x -> Rank.builder()
                        .team(x.getKey())
                        .evPoints(x.getValue().getLeft())
                        .points(x.getValue().getRight()).build())
                .sorted((o1, o2) -> Double.compare(o2.getEvPoints(), o1.getEvPoints()))
                .toList();
    }

}

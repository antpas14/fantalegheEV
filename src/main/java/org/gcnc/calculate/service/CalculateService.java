package org.gcnc.calculate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.gcnc.calculate.excel.ExcelService;
import org.gcnc.calculate.model.MatchResults;
import org.gcnc.calculate.parser.Parser;
import org.gcnc.fantalegheev_api.model.Rank;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.gcnc.calculate.util.MatchUtils.calculateMatchPoints;

@Component
@AllArgsConstructor
@Slf4j
public class CalculateService {
    private final ExcelService excelService;
    private final Parser parser;

    public Flux<Rank> calculateResponse(Flux<Part> file) {
        return file.flatMap(excelService::readExcel)
                .map(parser::getTeamResults)
                .flatMapIterable(this::calculateEVRank);
    }

    private List<Rank> calculateEVRank(List<MatchResults> results) {
        return results.stream()
            .filter(matchResult -> matchResult.results().getFirst().goal() != null)
            .flatMap(matchResult -> matchResult.results().stream()
                .map(t1 -> {
                    double points = matchResult.results().stream()
                        .filter(t2 -> !t2.team().equals(t1.team()))
                        .mapToDouble(t2 -> calculateMatchPoints(t1.goal(), t2.goal()))
                        .sum() / (matchResult.results().size() - 1); // Divide by number of opponents
                    return Pair.of(t1.team(), Pair.of(points, t1.points()));
                }))
            .collect(Collectors.groupingBy(
                Pair::getLeft,
                Collectors.reducing(
                    Pair.of(0D, 0),
                    Pair::getRight,
                    (p1, p2) -> Pair.of(p1.getLeft() + p2.getLeft(), p1.getRight() + p2.getRight())
                )
            ))
            .entrySet().stream()
            .map(entry -> Rank.builder()
                .team(entry.getKey())
                .evPoints(entry.getValue().getLeft())
                .points(entry.getValue().getRight())
                .build())
            .sorted(Comparator.comparingDouble(Rank::getEvPoints).reversed())
            .toList();
    }

}

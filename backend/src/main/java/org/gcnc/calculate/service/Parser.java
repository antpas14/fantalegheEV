package org.gcnc.calculate.service;

import org.gcnc.calculate.model.TeamResult;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface Parser {
    Mono<Map<Integer, List<TeamResult>>> getResults(String leagueName);
    Mono<Map<String, Integer>> getPoints(String leagueName);
}

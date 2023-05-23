package org.gcnc.calculate.service;

import org.gcnc.calculate.model.Rank;
import org.gcnc.calculate.model.Request;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CalculateService {
    Mono<List<Rank>> calculateResponse(Request req);
}

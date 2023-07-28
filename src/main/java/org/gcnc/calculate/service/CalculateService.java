package org.gcnc.calculate.service;

import org.gcnc.calculate.model.Request;
import org.gcnc.fantalegheev_api.model.Rank;
import reactor.core.publisher.Flux;

public interface CalculateService {
    Flux<Rank> calculateResponse(Request req);
}

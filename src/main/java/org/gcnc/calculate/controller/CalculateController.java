package org.gcnc.calculate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gcnc.calculate.model.Request;
import org.gcnc.calculate.service.CalculateService;
import org.gcnc.fantalegheev_api.api.CalculateApi;
import org.gcnc.fantalegheev_api.model.Rank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin
@Slf4j
@AllArgsConstructor
@RestController
public class CalculateController implements CalculateApi {
    private CalculateService calculateService;

    @Override
    public Mono<ResponseEntity<Flux<Rank>>> calculate(final String leagueName, final ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(calculateService.calculateResponse(new Request(leagueName))));
    }
}

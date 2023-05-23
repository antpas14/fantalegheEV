package org.gcnc.calculate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gcnc.calculate.model.Rank;
import org.gcnc.calculate.model.Request;
import org.gcnc.calculate.service.CalculateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@CrossOrigin
@Slf4j
@AllArgsConstructor
@RestController
public class CalculateController {
    private CalculateService calculateService;

    @RequestMapping(value = "/calculate", method = RequestMethod.GET)
    public Mono<ResponseEntity<Mono<List<Rank>>>> calculateGet(@RequestParam("league_name") String leagueName) {
        log.info("Called method /calculate");
        return Mono.just(ResponseEntity.ok(calculateService.calculateResponse(new Request(leagueName))));
    }
}

package org.gcnc.calculate.controller;

import org.gcnc.calculate.model.Rank;
import org.gcnc.calculate.model.Request;
import org.gcnc.calculate.service.CalculateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@CrossOrigin
@RestController
public class CalculateController {
    private CalculateService calculateService;
    public CalculateController(CalculateService calculateService) {
        this.calculateService = calculateService;
    }
    private final Logger logger = LoggerFactory.getLogger(CalculateController.class);

    @RequestMapping(value = "/calculate", method = RequestMethod.GET)
    public Mono<List<Rank>> calculateGet(@RequestParam("league_name") String leagueName) {
        logger.info("Called method /calculate");
        return calculateService.calculateResponse(Request.buildFor(leagueName));
    }
}

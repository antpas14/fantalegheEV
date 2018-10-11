package org.gcnc.calculate.controller;

import org.gcnc.calculate.model.Request;
import org.gcnc.calculate.model.Response;
import org.gcnc.calculate.service.CalculateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class CalculateController {

    @Autowired
    CalculateService calculateService;

    public CalculateController() {
    }
    private final Logger logger = LoggerFactory.getLogger(CalculateController.class);
    @RequestMapping(value = "/calculate", method = RequestMethod.POST)
    public Response calculate(@RequestBody Request req) {
        logger.info("Called method /calculate");
        return calculateService.calculateResponse(req);
    }

    @RequestMapping(value = "/calculate", method = RequestMethod.GET)
    public Response calculateGet(@RequestParam("league_name") String leagueName) {
        logger.info("Called method /calculate");
        return calculateService.calculateResponse(Request.buildFor(leagueName));
    }
}

package org.gcnc.calculate.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gcnc.calculate.service.CalculateService;
import org.gcnc.fantalegheev_api.api.CalculateApi;
import org.gcnc.fantalegheev_api.model.Rank;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
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

    @PostMapping("/calculate")
    @Override
    public Mono<ResponseEntity<Flux<Rank>>> calculate(@Parameter(name = "file",description = "",required = true) @RequestPart(value = "file",required = true) Part file, @Parameter(hidden = true) ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(calculateService.calculateResponse(file)));
    }
}

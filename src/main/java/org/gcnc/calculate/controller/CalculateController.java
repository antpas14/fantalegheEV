package org.gcnc.calculate.controller;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin
@Slf4j
@AllArgsConstructor
@RestController
public class CalculateController implements CalculateApi {
    private CalculateService calculateService;

    @PostMapping("/calculate")
    public Mono<ResponseEntity<Flux<Rank>>> calculate(@RequestPart("calendar") Part file) throws Exception {
        return Mono.just(ResponseEntity.ok(calculateService.calculateResponse(file)));
    }
}

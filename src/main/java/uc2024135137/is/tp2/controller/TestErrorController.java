package uc2024135137.is.tp2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.dto.request.RequestMediaCreate;
import uc2024135137.is.tp2.dto.request.RequestMediaUpdate;
import uc2024135137.is.tp2.model.Media;
import uc2024135137.is.tp2.service.MediaService;

@RestController
@RequestMapping("testerror")
public class TestErrorController {

    public TestErrorController() {

    }

    @GetMapping(value="/1")
    public Mono<Media> get() throws InterruptedException {
        return Mono.error(new RuntimeException("caralho mah fodam"));
    }

    @GetMapping(value="/2")
    public Mono<Media> get2() throws InterruptedException {
        Thread.sleep(5000);
        return Mono.error(new RuntimeException("caralho mah fodam"));
    }
}

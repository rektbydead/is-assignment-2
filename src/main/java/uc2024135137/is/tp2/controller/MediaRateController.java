package uc2024135137.is.tp2.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.dto.request.RequestMediaCreate;
import uc2024135137.is.tp2.dto.request.RequestMediaRateCreate;
import uc2024135137.is.tp2.dto.request.RequestMediaRateUpdate;
import uc2024135137.is.tp2.dto.request.RequestMediaUpdate;
import uc2024135137.is.tp2.model.Media;
import uc2024135137.is.tp2.model.MediaRate;
import uc2024135137.is.tp2.service.MediaRateService;
import uc2024135137.is.tp2.service.MediaService;

@RestController
@RequestMapping("mediarate")
public class MediaRateController {

    private final MediaRateService mediaRateService;

    public MediaRateController(MediaRateService mediaRateService) {
        this.mediaRateService = mediaRateService;
    }

    @PostMapping()
    public Mono<Object> create(@RequestBody @Valid RequestMediaRateCreate requestMediaRateCreate) {
        return mediaRateService.createMediaRate(requestMediaRateCreate)
                .onErrorResume(e ->
                        Mono.just(null
                        )
                );
    }

    @GetMapping(value = "/{media_rate_id}")
    public Mono<MediaRate> get(@PathVariable("media_rate_id") Long mediaRateId) {
        return mediaRateService.getMediaRateById(mediaRateId);
    }

    @GetMapping(value = "/")
    public Flux<MediaRate> getAll() {
        return mediaRateService.getAllMediaRates();
    }

    @PutMapping(value = "/{media_rate_id}")
    public Mono<MediaRate> update(@PathVariable("media_rate_id") Long userId, @RequestBody RequestMediaRateUpdate requestMediaRateUpdate) {
        return mediaRateService.updateMediaRate(userId, requestMediaRateUpdate);
    }

    @DeleteMapping(value = "/{media_id}")
    public Mono<Void> delete(@PathVariable("media_id") Long mediaRateId) {
        return mediaRateService.deleteMediaRateById(mediaRateId);
    }
}

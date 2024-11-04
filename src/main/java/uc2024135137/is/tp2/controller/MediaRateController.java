package uc2024135137.is.tp2.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaRateController.class);
    private final MediaRateService mediaRateService;

    public MediaRateController(MediaRateService mediaRateService) {
        this.mediaRateService = mediaRateService;
    }

    @PostMapping()
    public Mono<MediaRate> create(@RequestBody @Valid RequestMediaRateCreate requestMediaRateCreate) {
        LOGGER.info("Trying to create a new media query, request: {}", requestMediaRateCreate);
        return mediaRateService.createMediaRate(requestMediaRateCreate)
                .onErrorResume(Mono::error)
                .doOnSuccess(_-> LOGGER.info("Created a new mediarate sucessfully, request {}", requestMediaRateCreate));
    }

    @GetMapping(value = "/{media_rate_id}")
    public Mono<MediaRate> get(@PathVariable("media_rate_id") Long mediaRateId) {
        return mediaRateService.getMediaRateById(mediaRateId);
    }

    @GetMapping(value = "/")
    public Flux<MediaRate> getAll() {
        LOGGER.info("gettings all mediarates");
        return mediaRateService.getAllMediaRates()
                .onErrorResume(Mono::error)
                .doOnComplete(()-> LOGGER.info("All mediarates gotten sucessfully"));
    }

    @PutMapping(value = "/{media_rate_id}")
    public Mono<MediaRate> update(@PathVariable("media_rate_id") Long mediaRateId, @RequestBody RequestMediaRateUpdate requestMediaRateUpdate) {
        LOGGER.info("Trying to update a mediarate {}, request: {}", mediaRateId, requestMediaRateUpdate);
        return mediaRateService.updateMediaRate(mediaRateId, requestMediaRateUpdate)
                .onErrorResume(Mono::error)
                .doOnSuccess(_-> LOGGER.info("Updated mediarate {} successfully, request {}", mediaRateId, requestMediaRateUpdate));
    }

    @DeleteMapping(value = "/{media_id}")
    public Mono<Void> delete(@PathVariable("media_id") Long mediaRateId) {
        LOGGER.info("Trying to delete mediarate {}", mediaRateId);
        return mediaRateService.deleteMediaRateById(mediaRateId)
                .onErrorResume(Mono::error)
                .doOnSuccess(_-> LOGGER.info("mediarate {} delete sucessfully", mediaRateId));
    }
}

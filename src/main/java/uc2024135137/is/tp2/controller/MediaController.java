package uc2024135137.is.tp2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.dto.request.RequestMediaCreate;
import uc2024135137.is.tp2.dto.request.RequestMediaRateCreate;
import uc2024135137.is.tp2.dto.request.RequestMediaUpdate;
import uc2024135137.is.tp2.model.Media;
import uc2024135137.is.tp2.service.MediaService;

@RestController
@RequestMapping("media")
public class MediaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaController.class);
    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping()
    public Mono<Media> create(@RequestBody RequestMediaCreate requestMediaCreate) {
        LOGGER.info("Trying to create media, request {}", requestMediaCreate);
        return mediaService.createMedia(requestMediaCreate)
                .onErrorResume(Mono::error)
                .doOnSuccess(_-> LOGGER.info("Media created successfully, request: {}", requestMediaCreate));
    }

    @GetMapping(value = "/{media_id}")
    public Mono<Media> get(@PathVariable("media_id") Long userId) {
        return mediaService.findMediaById(userId);
    }

    @GetMapping(value = "/")
    public Flux<Media> getAll() {
        LOGGER.info("Getting all medias");
        return mediaService.findAllMedias()
                .onErrorResume(Mono::error)
                .doOnComplete(()-> LOGGER.info("All Medias gotten successfully"));
    }

    @PutMapping(value = "/{media_id}")
    public Mono<Media> update(@PathVariable("media_id") Long mediaId, @RequestBody RequestMediaUpdate requestMediaUpdate) {
        return mediaService.updateMedia(mediaId, requestMediaUpdate)
                .onErrorResume(Mono::error)
                .doOnSuccess(_-> LOGGER.info("Media {} updated successfully, request: {}", mediaId, requestMediaUpdate));
    }

    @DeleteMapping(value = "/{media_id}")
    public Mono<Void> delete(@PathVariable("media_id") Long mediaId) {
        return mediaService.deleteMediaById(mediaId)
                .onErrorResume(Mono::error)
                .doOnSuccess(_-> LOGGER.info("Media {} deleted successfully", mediaId));
    }
}

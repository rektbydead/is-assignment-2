package uc2024135137.is.tp2.controller;

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

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping()
    public Mono<Media> create(@RequestBody RequestMediaCreate requestMediaCreate) {
        return mediaService.createMedia(requestMediaCreate)
                .onErrorResume(e ->
                        Mono.just(null
                        )
                );
    }

    @GetMapping(value = "/{media_id}")
    public Mono<Media> get(@PathVariable("media_id") Long userId) {
        return mediaService.findMediaById(userId);
    }

    @GetMapping(value = "/")
    public Flux<Media> getAll() {
        return mediaService.findAllMedias();
    }

    @PutMapping(value = "/{media_id}")
    public Mono<Media> update(@PathVariable("media_id") Long userId, @RequestBody RequestMediaUpdate requestMediaUpdate) {
        return mediaService.updateMedia(userId, requestMediaUpdate);
    }

    @DeleteMapping(value = "/{media_id}")
    public Mono<Void> delete(@PathVariable("media_id") Long userId) {
        return mediaService.deleteMediaById(userId);
    }
}

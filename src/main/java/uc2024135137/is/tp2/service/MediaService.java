package uc2024135137.is.tp2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.controller.MediaRateController;
import uc2024135137.is.tp2.dto.request.RequestMediaCreate;
import uc2024135137.is.tp2.dto.request.RequestMediaRateCreate;
import uc2024135137.is.tp2.dto.request.RequestMediaUpdate;
import uc2024135137.is.tp2.model.Media;
import uc2024135137.is.tp2.repository.MediaRepository;

@Service
public class MediaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaService.class);
    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public Flux<Media> findMediaByTitle(String title) {
        return mediaRepository.findMediaByTitle(title);
    }

    public Mono<Media> findMediaById(Long id) {
        return mediaRepository.findById(id);
    }

    public Flux<Media> findAllMedias() {
        return mediaRepository.findAll();
    }

    public Mono<Media> createMedia(RequestMediaCreate requestMediaCreate) {
        Media media = new Media();
        media.setMediaType(requestMediaCreate.getMediaType().getValue());
        media.setReleaseDate(requestMediaCreate.getReleaseDate());
        media.setTitle(requestMediaCreate.getTitle());
        return mediaRepository.save(media).doOnError(e -> LOGGER.error("Error creating media request: {} - {}", requestMediaCreate, e.getMessage()));
    }

    public Mono<Media> updateMedia(Long id, RequestMediaUpdate requestMediaUpdate) {
        return mediaRepository.findMediaById(id).flatMap(media -> {
            media.setMediaType(requestMediaUpdate.getMediaType().getValue());
            media.setTitle(requestMediaUpdate.getTitle());
            return mediaRepository.save(media)
                    .doOnError(e -> LOGGER.error("Error updating media {}: {}", id, e.getMessage()));
        }).switchIfEmpty(Mono.defer(() -> {
                    LOGGER.warn("Error finding media to update by id {}", id);
                    return Mono.error(new RuntimeException("Error finding media to update by id " + id));
                })
        );
    }

    public Mono<Void> deleteMediaById(Long id) {
        return mediaRepository.findById(id).flatMap(media -> {
            if (media.getNumberOfRates() > 0) {
                LOGGER.warn("Error deleting media {} because it has been rated.", id);
                return Mono.error(new RuntimeException("Media is rated and cannot be deleted"));
            }

            return mediaRepository.deleteById(id).doOnError(e -> LOGGER.error("Error deleting media {}: {}", id, e.getMessage()));
        }).switchIfEmpty(Mono.defer(() -> {
                    LOGGER.warn("Error finding media to delete by id {}", id);
                    return Mono.error(new RuntimeException("Error finding media to delete by id " + id));
                })
        );
    }
}

package uc2024135137.is.tp2.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.dto.request.RequestMediaCreate;
import uc2024135137.is.tp2.dto.request.RequestMediaRateCreate;
import uc2024135137.is.tp2.dto.request.RequestMediaUpdate;
import uc2024135137.is.tp2.model.Media;
import uc2024135137.is.tp2.repository.MediaRepository;

@Service
public class MediaService {

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
        media.setMediaType(requestMediaCreate.getMediaType());
        media.setReleaseDate(requestMediaCreate.getReleaseDate());
        media.setTitle(requestMediaCreate.getTitle());
        return mediaRepository.save(media);
    }

    public Mono<Media> updateMedia(Long id, RequestMediaUpdate requestMediaUpdate) {
        return mediaRepository.findMediaById(id).flatMap(media -> {
            media.setMediaType(requestMediaUpdate.getMediaType());
            media.setTitle(requestMediaUpdate.getTitle());
            return mediaRepository.save(media);
        });
    }

    public Mono<Void> deleteMediaById(Long id) {
        return mediaRepository.findById(id).flatMap(media -> {
            if (media.getNumberOfRates() > 0) {
                return Mono.error(new RuntimeException("User has rated media and cannot be deleted"));
            }

            return mediaRepository.deleteById(id);
        });
    }
}

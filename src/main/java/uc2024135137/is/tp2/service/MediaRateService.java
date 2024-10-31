package uc2024135137.is.tp2.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.dto.request.RequestMediaRateCreate;
import uc2024135137.is.tp2.dto.request.RequestMediaRateUpdate;
import uc2024135137.is.tp2.model.Media;
import uc2024135137.is.tp2.model.MediaRate;
import uc2024135137.is.tp2.model.User;
import uc2024135137.is.tp2.repository.MediaRateRepository;
import uc2024135137.is.tp2.repository.MediaRepository;
import uc2024135137.is.tp2.repository.UserRepository;

@Service
public class MediaRateService {

    private final MediaRateRepository mediaRateRepository;
    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;

    public MediaRateService(MediaRateRepository mediaRateRepository, MediaRepository mediaRepository, UserRepository userRepository) {
        this.mediaRateRepository = mediaRateRepository;
        this.mediaRepository = mediaRepository;
        this.userRepository = userRepository;
    }

    public Mono<Object> createMediaRate(RequestMediaRateCreate requestMediaCreate) {
        return mediaRateRepository.findMediaRateByUserIdAndMediaId(
                requestMediaCreate.getUserId(),
                requestMediaCreate.getMediaId()
        ).flatMap(mediaRate ->
                Mono.error(new RuntimeException("Relationship already exists"))
        ).switchIfEmpty(Mono.defer(() ->
             userRepository.incrementUserNumberOfRatedMedia(requestMediaCreate.getUserId())
                    .then(mediaRepository.incrementMediaNumberOfRates(requestMediaCreate.getMediaId()).retry())
                    .then(mediaRepository.sumMediaTotalRating(requestMediaCreate.getMediaId(), requestMediaCreate.getRate()).retry())
                    .then(Mono.defer(() -> {
                        MediaRate mediaRate = new MediaRate();
                        mediaRate.setUserId(requestMediaCreate.getUserId());
                        mediaRate.setMediaId(requestMediaCreate.getMediaId());
                        mediaRate.setRating(requestMediaCreate.getRate());
                        return mediaRateRepository.save(mediaRate);
                    }))
        ));
    }

    public Mono<MediaRate> updateMediaRate(Long id, RequestMediaRateUpdate requestMediaRateUpdate) {
        return mediaRateRepository.findById(id).flatMap(mediaRate ->
            mediaRepository.reduceMediaTotalRating(mediaRate.getId(), mediaRate.getRating())
                    .then(mediaRepository.sumMediaTotalRating(mediaRate.getId(), requestMediaRateUpdate.getRate()))
                    .then(Mono.just(mediaRate))
        );
    }

    public Mono<Void> deleteMediaRateById(Long id) {
        return mediaRateRepository.findById(id).flatMap(mediaRate ->
            userRepository.decrementUserNumberOfRatedMedia(mediaRate.getId())
                    .then(mediaRepository.decrementMediaNumberOfRates(mediaRate.getId()).retry())
                    .then(mediaRepository.reduceMediaTotalRating(mediaRate.getId(), mediaRate.getRating()).retry())
                    .then(mediaRateRepository.delete(mediaRate))
        );
    }

    public Mono<MediaRate> getMediaRateById(Long id) {
        return mediaRateRepository.findById(id);
    }

    public Flux<MediaRate> getAllMediaRates() {
        return mediaRateRepository.findAll();
    }
}

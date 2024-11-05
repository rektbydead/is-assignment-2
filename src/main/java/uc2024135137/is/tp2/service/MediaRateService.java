package uc2024135137.is.tp2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaRateService.class);
    private final MediaRateRepository mediaRateRepository;
    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;

    public MediaRateService(MediaRateRepository mediaRateRepository, MediaRepository mediaRepository, UserRepository userRepository) {
        this.mediaRateRepository = mediaRateRepository;
        this.mediaRepository = mediaRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Mono<MediaRate> createMediaRate(RequestMediaRateCreate requestMediaCreate) {
        return mediaRateRepository.findMediaRateByUserIdAndMediaId(
                requestMediaCreate.getUserId(),
                requestMediaCreate.getMediaId()
        ).hasElement().flatMap(element -> {
            if (element) {
                LOGGER.warn("User trying to rate an already rated media");
                return Mono.error(new RuntimeException("Relationship already exists"));
            }

            return userRepository.incrementUserNumberOfRatedMedia(requestMediaCreate.getUserId())
                    .then(mediaRepository.incrementMediaNumberOfRates(requestMediaCreate.getMediaId())
                            .doOnSuccess(_ -> LOGGER.info("Incremented number of rates for mediaId: {}", requestMediaCreate.getMediaId()))
                            .doOnError(_ -> LOGGER.error("Error trying to increment number of rates for mediaId: {}", requestMediaCreate.getMediaId())))
                    .then(mediaRepository.sumMediaTotalRating(requestMediaCreate.getMediaId(), requestMediaCreate.getRate())
                            .doOnSuccess(_ -> LOGGER.info("Increased the media total rating for mediaId: {}", requestMediaCreate.getMediaId()))
                            .doOnError(_ -> LOGGER.error("Error trying to increase the media total rating for mediaId: {}", requestMediaCreate.getMediaId())))
                    .then(Mono.defer(() -> {
                        MediaRate mediaRate = new MediaRate();
                        mediaRate.setUserId(requestMediaCreate.getUserId());
                        mediaRate.setMediaId(requestMediaCreate.getMediaId());
                        mediaRate.setRating(requestMediaCreate.getRate());
                        return mediaRateRepository.save(mediaRate)
                                .doOnSuccess(_ -> LOGGER.info("mediaId {} Rated", requestMediaCreate.getMediaId()))
                                .doOnError(e -> LOGGER.error("error rating mediaId {} - {}", requestMediaCreate.getMediaId(), e.getMessage()));
                    }));
        });
    }

    @Transactional
    public Mono<MediaRate> updateMediaRate(Long id, RequestMediaRateUpdate requestMediaRateUpdate) {
        return mediaRateRepository.findById(id).flatMap(mediaRate ->
                mediaRepository.reduceMediaTotalRating(mediaRate.getId(), mediaRate.getRating())
                        .doOnSuccess(_ -> LOGGER.info("reducing {} rate", mediaRate.getMediaId()))
                        .doOnError(_ -> LOGGER.error("error reducing mediaId {} rate", mediaRate.getMediaId()))
                        .then(mediaRepository.sumMediaTotalRating(mediaRate.getId(), requestMediaRateUpdate.getRate())
                                .doOnSuccess(_ -> LOGGER.info("increasing mediaId {} rate", mediaRate.getMediaId()))
                                .doOnError(_ -> LOGGER.error("error increasing mediaId {} rate", mediaRate.getMediaId())))
                        .then(Mono.just(mediaRate))
        ).switchIfEmpty(Mono.defer(() -> {
                LOGGER.warn("Error finding media rate to update by id {}", id);
                return Mono.error(new RuntimeException("Error finding media rate to update by id " + id));
            })
        );
    }

    @Transactional
    public Mono<Void> deleteMediaRateById(Long id) {
        return mediaRateRepository.findById(id).flatMap(mediaRate ->
                userRepository.decrementUserNumberOfRatedMedia(mediaRate.getUserId())
                        .doOnSuccess(_ -> LOGGER.info("decrementing number of rated medias for user {}", mediaRate.getUserId()))
                        .doOnError(_ -> LOGGER.error("error decrementing number of rated medias for user {}", mediaRate.getUserId()))
                        .then(mediaRepository.decrementMediaNumberOfRates(mediaRate.getId())
                                .doOnSuccess(_ -> LOGGER.info("decrementing number of rates for media {}", mediaRate.getMediaId()))
                                .doOnError(_ -> LOGGER.error("error decrementing  number of rates for media {}", mediaRate.getMediaId())))
                        .then(mediaRepository.reduceMediaTotalRating(mediaRate.getId(), mediaRate.getRating())
                                .doOnSuccess(_ -> LOGGER.info("reducing media total rating for media {}", mediaRate.getMediaId()))
                                .doOnError(_ -> LOGGER.error("error reducing media total rating for media  {}", mediaRate.getMediaId())))
                        .then(mediaRateRepository.delete(mediaRate)
                                .doOnSuccess(_ -> LOGGER.info("deleting mediarate {}", mediaRate.getId()))
                                .doOnError(_ -> LOGGER.error("error deleting mediarate {}", mediaRate.getId())))
        ).switchIfEmpty(Mono.defer(() -> {
                LOGGER.warn("Error finding media rate to delete by id {}", id);
                return Mono.error(new RuntimeException("Error finding media rate to delete by id " + id));
            })
        );
    }

    public Mono<MediaRate> getMediaRateById(Long id) {
        return mediaRateRepository.findById(id);
    }

    public Flux<MediaRate> getAllMediaRates() {
        return mediaRateRepository.findAll();
    }
}

package uc2024135137.is.tp2.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.model.Media;
import uc2024135137.is.tp2.model.MediaRate;
import uc2024135137.is.tp2.model.User;


@Repository
public interface MediaRateRepository extends R2dbcRepository<MediaRate, Long> {

    Mono<MediaRate> findMediaRateByUserIdAndMediaId(Long userId, Long mediaId);
}

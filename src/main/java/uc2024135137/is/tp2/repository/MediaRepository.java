package uc2024135137.is.tp2.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.model.Media;
import uc2024135137.is.tp2.model.User;


@Repository
public interface MediaRepository extends R2dbcRepository<Media, Long> {

    Mono<Media> findMediaById(Long id);

    Flux<Media> findMediaByTitle(String title);

    @Transactional
    @Modifying
    @Query("UPDATE Media SET NumberOfRates = NumberOfRates + 1, version = version + 1 where id=:id")
    Mono<Void> incrementMediaNumberOfRates(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Media SET NumberOfRates = NumberOfRates - 1, version = version + 1 where id=:id")
    Mono<Void> decrementMediaNumberOfRates(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Media SET totalRating = totalRating + :rating, version = version + 1 where id=:id")
    Mono<Void> sumMediaTotalRating(Long id, double rating);

    @Transactional
    @Modifying
    @Query("UPDATE Media SET totalRating = totalRating - :rating, version = version + 1 where id=:id")
    Mono<Void> reduceMediaTotalRating(Long id, double rating);
}

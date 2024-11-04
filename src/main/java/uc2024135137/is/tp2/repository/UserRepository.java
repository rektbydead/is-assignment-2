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
import uc2024135137.is.tp2.model.User;


@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    Mono<User> findUserById(Long id);

    Flux<User> findUserByName(String name);

    @Transactional
    @Modifying
    @Query("UPDATE USERS SET numberOfRatedMedia = numberOfRatedMedia + 1, version = version + 1 WHERE id = :id")
    Mono<Void> incrementUserNumberOfRatedMedia(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE USERS SET numberOfRatedMedia = numberOfRatedMedia - 1, version = version + 1 WHERE id=:id")
    Mono<Void> decrementUserNumberOfRatedMedia(Long id);
}

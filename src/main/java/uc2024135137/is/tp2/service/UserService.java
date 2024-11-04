package uc2024135137.is.tp2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.dto.request.RequestUserCreate;
import uc2024135137.is.tp2.dto.request.RequestUserUpdate;
import uc2024135137.is.tp2.model.User;
import uc2024135137.is.tp2.repository.UserRepository;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flux<User> findUserByName(String name) {
        return userRepository.findUserByName(name);
    }

    public Mono<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Flux<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> createUser(RequestUserCreate requestUserCreate) {
        User user = new User();
        user.setName(requestUserCreate.getName());
        user.setAge(requestUserCreate.getAge());
        user.setGender(requestUserCreate.getGender().getValue());
        user.setNumberOfRatedMedia(0L);
        return userRepository.save(user).doOnError(e -> LOGGER.error("Error creating user request {}: {}", requestUserCreate, e.getMessage()));
    }

    public Mono<User> updateUser(Long id, RequestUserUpdate requestUserUpdate) {
        return userRepository.findUserById(id).flatMap(user -> {
            user.setName(requestUserUpdate.getName());
            user.setAge(requestUserUpdate.getAge());
            user.setGender(requestUserUpdate.getGender().getValue());
            return userRepository.save(user).doOnError(e -> LOGGER.error("Error updating user {}: {}", id, e.getMessage()));
        }).switchIfEmpty(Mono.defer(() -> {
                    LOGGER.warn("Error finding user to update by id {}", id);
                    return Mono.error(new RuntimeException("Error finding user to update by id " + id));
                })
        );
    }

    public Mono<Void> deleteUserById(Long id) {
        return userRepository.findById(id).flatMap(user -> {
            if (user.getNumberOfRatedMedia() > 0) {
                LOGGER.warn("Error delete user {} because he has rated medias", id);
                return Mono.error(new RuntimeException("User has rated media and cannot be deleted"));
            }

            return userRepository.deleteById(id).doOnError(e -> LOGGER.error("Error deleting media {}: {}", id, e.getMessage()));
        });
    }
}

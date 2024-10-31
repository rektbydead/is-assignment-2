package uc2024135137.is.tp2.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.dto.request.RequestUserCreate;
import uc2024135137.is.tp2.dto.request.RequestUserUpdate;
import uc2024135137.is.tp2.model.User;
import uc2024135137.is.tp2.repository.UserRepository;

@Service
public class UserService {

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
        user.setGender(requestUserCreate.getGender());
        user.setNumberOfRatedMedia(0L);
        return userRepository.save(user);
    }

    public Mono<User> updateUser(Long id, RequestUserUpdate requestUserUpdate) {
        return userRepository.findUserById(id).flatMap(user -> {
            user.setName(requestUserUpdate.getName());
            user.setAge(requestUserUpdate.getAge());
            user.setGender(requestUserUpdate.getGender());
            return userRepository.save(user);
        });
    }

    public Mono<Void> deleteUserById(Long id) {
        return userRepository.findById(id).flatMap(user -> {
            if (user.getNumberOfRatedMedia() > 0) {
                return Mono.error(new RuntimeException("User has rated media and cannot be deleted"));
            }

            return userRepository.deleteById(id);
        });
    }
}

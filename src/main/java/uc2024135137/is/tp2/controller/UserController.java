package uc2024135137.is.tp2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.dto.request.RequestUserCreate;
import uc2024135137.is.tp2.dto.request.RequestUserUpdate;
import uc2024135137.is.tp2.model.User;
import uc2024135137.is.tp2.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public Mono<User> create(@RequestBody RequestUserCreate requestUserCreate) {
        LOGGER.info("Trying to create a user, request: {}", requestUserCreate);
        return userService.createUser(requestUserCreate)
                .onErrorResume(Mono::error)
                .doOnSuccess(_-> LOGGER.info("user {} created sucessfully"));
    }

    @GetMapping(value = "/{user_id}")
    public Mono<User> get(@PathVariable("user_id") Long userId) {
        return userService.findUserById(userId);
    }

    @GetMapping(value = "/")
    public Flux<User> getAll() {
        LOGGER.info("Getting all users");
        return userService.findAllUsers()
                .onErrorResume(Mono::error)
                .doOnComplete(()-> LOGGER.info("All users gotten sucessfully"));
    }

    @PutMapping(value = "/{user_id}")
    public Mono<User> update(@PathVariable("user_id") Long userId, @RequestBody RequestUserUpdate requestUserUpdate) {
        LOGGER.info("Trying to update a user, request: {}", requestUserUpdate);
        return userService.updateUser(userId, requestUserUpdate)
                .onErrorResume(Mono::error)
                .doOnSuccess(_-> LOGGER.info("user {} updated sucessfully", userId));
    }

    @DeleteMapping(value = "/{user_id}")
    public Mono<Void> delete(@PathVariable("user_id") Long userId) {
        LOGGER.info("Trying to delete user {}", userId);
        return userService.deleteUserById(userId)
                .onErrorResume(Mono::error)
                .doOnSuccess(_-> LOGGER.info("user {} delete sucessfully", userId));
    }
}

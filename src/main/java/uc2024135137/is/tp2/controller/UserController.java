package uc2024135137.is.tp2.controller;

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

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public Mono<User> create(@RequestBody RequestUserCreate requestUserCreate) {
        return userService.createUser(requestUserCreate)
                .onErrorResume(e ->
                        Mono.just(null
                        )
                );
    }

    @GetMapping(value = "/{user_id}")
    public Mono<User> get(@PathVariable("user_id") Long userId) {
        return userService.findUserById(userId);
    }

    @GetMapping(value = "/")
    public Flux<User> getAll() {
        return userService.findAllUsers();
    }

    @PutMapping(value = "/{user_id}")
    public Mono<User> update(@PathVariable("user_id") Long userId, @RequestBody RequestUserUpdate requestUserUpdate) {
        return userService.updateUser(userId, requestUserUpdate);
    }

    @DeleteMapping(value = "/{user_id}")
    public Mono<Void> delete(@PathVariable("user_id") Long userId) {
        return userService.deleteUserById(userId);
    }
}

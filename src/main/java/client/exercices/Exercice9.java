package client.exercices;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;
import uc2024135137.is.tp2.model.Media;

import client.Client;
import reactor.core.publisher.Flux;
import uc2024135137.is.tp2.model.Media;
import uc2024135137.is.tp2.model.MediaRate;
import uc2024135137.is.tp2.model.User;

import java.util.*;
import java.util.concurrent.Callable;

public class Exercice9 implements Callable<Flux<?>> {

    private final WebClient webClient;
    public Exercice9(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<String> call() throws Exception {
        Flux<Media> mediaFlux = webClient.get()
                .uri("/media/")
                .retrieve()
                .bodyToFlux(Media.class);

        Mono<Map<Long, List<MediaRate>>> mediaRateMono = webClient.get()
                .uri("/mediarate/")
                .retrieve()
                .bodyToFlux(MediaRate.class)
                .reduce(new HashMap<>(), (map, mediaRate) -> {
                    map.computeIfAbsent(mediaRate.getMediaId(), _ -> new ArrayList<>()).add(mediaRate);
                    return map;
                });

        Mono<Map<Long, User>> userListMono = webClient.get()
                .uri("/user/")
                .retrieve()
                .bodyToFlux(User.class)
                .reduce(new HashMap<>(), (map, user) -> {
                    map.put(user.getId(), user);
                    return map;
                });

        return Flux.zip(mediaRateMono, userListMono).flatMap(tuple ->
            mediaFlux.flatMap(media -> {
                List<MediaRate> mediaRateList = tuple.getT1().getOrDefault(media.getId(), new ArrayList<>());
                List<User> userList = new ArrayList<>();

                for (MediaRate mediaRate : mediaRateList) {
                    userList.add(tuple.getT2().get(mediaRate.getUserId()));
                }

                userList.sort((user1, user2) -> Short.compare(user2.getAge(), user1.getAge()));

                StringBuilder stringBuilder = new StringBuilder("(" + media.getId() + ") " + media.getTitle() + "; Users (" + mediaRateList.size() + "): ");

                for (User user : userList) {
                    stringBuilder.append(user.getName()).append("(").append(user.getAge()).append("), ");
                }

                return Mono.just(stringBuilder.toString());
            })
        );
    }
}

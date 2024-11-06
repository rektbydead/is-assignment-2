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

                StringBuilder stringBuilder = new StringBuilder("(" + media.getId() + ") " + media.getTitle() + "; Users (" + mediaRateList.size() + "): ");

                for (MediaRate mediarate : mediaRateList) {
                    User user = tuple.getT2().get(mediarate.getUserId());
                    stringBuilder.append(user.getName()).append(", ");
                }

                return Mono.just(stringBuilder.toString());
            })
        );
    }
}

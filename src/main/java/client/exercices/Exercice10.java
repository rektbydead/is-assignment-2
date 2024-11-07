package client.exercices;

import client.Client;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.model.Media;
import uc2024135137.is.tp2.model.MediaRate;
import uc2024135137.is.tp2.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class Exercice10 implements Callable<Flux<?>> {

    private final WebClient webClient;
    public Exercice10(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<Object> call() throws Exception {
        Flux<User> userFlux = webClient.get()
                .uri("/user/")
                .retrieve()
                .bodyToFlux(User.class);

        Mono<Map<Long, List<MediaRate>>> mediaRateFlux = webClient.get()
                .uri("/mediarate/")
                .retrieve()
                .bodyToFlux(MediaRate.class)
                .reduce(new ConcurrentHashMap<>(), (map, mediaRate) -> {
                    map.computeIfAbsent(mediaRate.getUserId(), _ -> new ArrayList<>()).add(mediaRate);
                    return map;
                });

        Mono<Map<Long, Media>> mediaMapMono = webClient.get()
                .uri("/media/")
                .retrieve()
                .bodyToFlux(Media.class)
                .reduce(new ConcurrentHashMap<>(), (map, media) -> {
                    map.put(media.getId(), media);
                    return map;
                });

        return Flux.zip(mediaRateFlux, mediaMapMono).flatMap(tuple ->
            userFlux.flatMap(user -> {
                List<MediaRate> mediaRateList = tuple.getT1().getOrDefault(user.getId(), new ArrayList<>());

                StringBuilder stringBuilder = new StringBuilder("(" + user.getId() + ") " + user.getName() + "; Medias (" + mediaRateList.size() + "): ");
                for (MediaRate mediarate : mediaRateList) {
                    Media media = tuple.getT2().get(mediarate.getUserId());
                    stringBuilder.append(media.getTitle()).append(", ");
                }

                return Mono.just(stringBuilder.toString());
            })
        );
    }

}

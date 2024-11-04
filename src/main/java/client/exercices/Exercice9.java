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

public class Exercice9 implements Callable<Flux<Object>> {

    private final WebClient webClient;
    public Exercice9(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<Object> call() throws Exception {
        Map<Long, Media> mediaHashMap = new HashMap<>();
        Map<Long, User> userHashMap = new HashMap<>();
        Map<Long, List<User>> usersThatRatedMedia = new HashMap<>();

        Flux<Media> mediaFlux = webClient.get()
                .uri("/media/")
                .retrieve()
                .bodyToFlux(Media.class)
                .doOnNext(media -> mediaHashMap.put(media.getId(), media));


        Flux<User> userFlux = webClient.get()
                .uri("/user/")
                .retrieve()
                .bodyToFlux(User.class)
                .doOnNext(user -> userHashMap.put(user.getId(), user));

        return Flux.merge(mediaFlux, userFlux).thenMany(
                webClient.get()
                    .uri("/mediarate/")
                    .retrieve()
                    .bodyToFlux(MediaRate.class)
                    .doOnNext(mediaRate -> {
                        if (!usersThatRatedMedia.containsKey(mediaRate.getMediaId())) {
                            usersThatRatedMedia.put(mediaRate.getMediaId(), new ArrayList<>());
                        }

                        User user = userHashMap.get(mediaRate.getUserId());
                        usersThatRatedMedia.get(mediaRate.getMediaId()).add(user);
                    })
                    .thenMany(Flux.fromIterable(usersThatRatedMedia.entrySet())
                                    .flatMap(entry -> {
                                        Long mediaId = entry.getKey();
                                        List<User> users = entry.getValue();

                                        users.sort((user1, user2) -> user2.getAge().compareTo(user1.getAge()));

                                        Media media = mediaHashMap.get(mediaId);
                                        StringBuilder stringBuilder = new StringBuilder("(" + media.getId() + ") " + media.getTitle() + "; Users (" + users.size() + "): ");

                                        for (User user : users) {
                                            stringBuilder.append(user.getName()).append(" (").append(user.getAge()).append("), ");
                                        }

                                        return Flux.just(stringBuilder.toString());
                                })
                    )
        );
    }
}

package client.exercices;

import client.Client;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import uc2024135137.is.tp2.model.Media;
import uc2024135137.is.tp2.model.MediaRate;
import uc2024135137.is.tp2.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class Exercice10 implements Callable<Flux<?>> {

    private final WebClient webClient;
    public Exercice10(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<String> call() throws Exception {
        Map<Long, Media> mediaHashMap = new HashMap<>();
        Map<Long, User> userHashMap = new HashMap<>();
        Map<Long, List<Media>> mediasRatedByUser = new HashMap<>();

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
                        if (!mediasRatedByUser.containsKey(mediaRate.getUserId())) {
                            mediasRatedByUser.put(mediaRate.getUserId(), new ArrayList<>());
                        }

                        Media media = mediaHashMap.get(mediaRate.getMediaId());
                        mediasRatedByUser.get(mediaRate.getUserId()).add(media);
                    })
                    .thenMany(Flux.fromIterable(mediasRatedByUser.entrySet())
                                    .flatMap(entry -> {
                                        Long userId = entry.getKey();
                                        List<Media> medias = entry.getValue();

                                        User user = userHashMap.get(userId);
                                        StringBuilder stringBuilder = new StringBuilder("(" + user.getId() + ") " + user.getName() + "; Medias (" + medias.size() + "): ");

                                        for (Media media : medias) {
                                            stringBuilder.append(media.getTitle()).append(", ");
                                        }

                                        return Flux.just(stringBuilder.toString());
                                })
                    )
        );
    }
}

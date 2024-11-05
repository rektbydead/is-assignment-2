package client.exercices;

import client.Client;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import uc2024135137.is.tp2.model.Media;
import uc2024135137.is.tp2.model.MediaRate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class Exercice4 implements Callable<Flux<?>> {

    private final WebClient webClient;
    public Exercice4(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<Long> call() throws Exception {
        return webClient.get()
                .uri("/media/")
                .retrieve()
                .bodyToFlux(Media.class)
                .filter(media -> media.getNumberOfRates() > 0).count().flux();
    }

    // @Override
    // public Flux<Long> call() throws Exception {
    //     ConcurrentHashMap<Long, Long> mediaRateCountMap = new ConcurrentHashMap<>();
//
    //     Flux<Media> mediaFlux = webClient.get()
    //             .uri("/media/")
    //             .retrieve()
    //             .bodyToFlux(Media.class);
//
    //     Flux<MediaRate> mediaRateFlux = webClient.get()
    //             .uri("/mediarate/")
    //             .retrieve()
    //             .bodyToFlux(MediaRate.class)
    //             .doOnNext(mediaRate ->
    //                     mediaRateCountMap.merge(mediaRate.getMediaId(), 1L, Long::sum)
    //             );
//
    //     return Flux.merge(mediaFlux, mediaRateFlux)
    //             .thenMany(Flux.fromIterable(mediaRateCountMap.keySet())
    //             .filter(mediaId -> mediaRateCountMap.getOrDefault(mediaId, 0L) > 0)
    //             .count());
    // }
}

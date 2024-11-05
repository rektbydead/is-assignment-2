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

public class Exercice3 implements Callable<Flux<?>> {

    private final WebClient webClient;
    public Exercice3(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<Long> call() throws Exception {
        return webClient.get()
                .uri("/media/")
                .retrieve()
                .bodyToFlux(Media.class)
                .filter(media -> media.getAverageRating() > 8).count().flux();
    }

    // @Override
    // public Flux<Long> call() throws Exception {
    //     List<Media> mediaList = new ArrayList<>();
    //     Map<Long, List<MediaRate>> mediasRateByUser = new HashMap<>();
//
    //     Flux<Media> mediaFlux = webClient.get()
    //             .uri("/media/")
    //             .retrieve()
    //             .bodyToFlux(Media.class).doOnNext(mediaList::add);
//
    //     Flux<MediaRate> mediaRateFlux = webClient.get()
    //             .uri("/mediarate/")
    //             .retrieve()
    //             .bodyToFlux(MediaRate.class).doOnNext(mediaRate -> {
    //                 if (!mediasRateByUser.containsKey(mediaRate.getMediaId())) {
    //                     mediasRateByUser.put(mediaRate.getMediaId(), new ArrayList<>());
    //                 }
//
    //                 mediasRateByUser.get(mediaRate.getMediaId()).add(mediaRate);
    //             });
//
    //     return Flux.merge(mediaFlux, mediaRateFlux).thenMany(Flux.fromIterable(mediaList).filter(media -> {
    //         if (!mediasRateByUser.containsKey(media.getId())) return false;
//
    //         List<MediaRate> mediaRates = mediasRateByUser.get(media.getId());
    //         double totalRating = 0;
//
    //         for (MediaRate mediaRate : mediaRates) {
    //             totalRating += mediaRate.getRating();
    //         }
//
    //         return (totalRating / mediaRates.size()) > 8;
    //     }).count());
    // }
}

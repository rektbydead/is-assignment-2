package client.exercices;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.model.Media;
import uc2024135137.is.tp2.model.MediaRate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class Exercice5 implements Callable<Flux<?>> {

    private final LocalDate FIRST_DATE = LocalDate.of(1980, 1, 1);
    private final LocalDate LAST_DATE = LocalDate.of(1989, 12, 31);

    private final WebClient webClient;
    public Exercice5(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<String> call() throws Exception {
        return webClient.get()
                .uri("/media/")
                .retrieve()
                .bodyToFlux(Media.class)
                .filter(media -> {
                    LocalDate releaseDate = media.getReleaseDate();
                    return releaseDate.isAfter(FIRST_DATE) && releaseDate.isBefore(LAST_DATE);
                }).sort((media1, media2) -> Double.compare(media2.getAverageRating(), media1.getAverageRating()))
                .flatMap((media) ->
                    Mono.just(String.format("%s - %.2f", media.getTitle(), media.getAverageRating()))
                );
    }

    // @Override
    // public Flux<String> call() throws Exception {
    //     List<Media> mediaList = new ArrayList<>();
    //     Map<Long, List<MediaRate>> mediasRateByUser = new HashMap<>();
//
    //     Flux<Media> mediaFlux = webClient.get()
    //             .uri("/media/")
    //             .retrieve()
    //             .bodyToFlux(Media.class)
    //             .filter(media -> {
    //                 LocalDate releaseDate = media.getReleaseDate();
    //                 return releaseDate.isAfter(FIRST_DATE) && releaseDate.isBefore(LAST_DATE);
    //             }).doOnNext(mediaList::add);
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
    //     return Flux.merge(mediaFlux, mediaRateFlux).thenMany(Flux.fromIterable(mediaList).filter(media ->
    //         mediasRateByUser.containsKey(media.getId())
    //     ).sort((media1, media2) -> {
    //         List<MediaRate> mediaRates = mediasRateByUser.getOrDefault(media1.getId(), new ArrayList<>());
    //         List<MediaRate> mediaRates2 = mediasRateByUser.getOrDefault(media2.getId(), new ArrayList<>());
//
    //         double totalRating1 = 0;
    //         double totalRating2 = 0;
//
    //         for (MediaRate mediaRate : mediaRates) {
    //             totalRating1 += mediaRate.getRating();
    //         }
//
    //         for (MediaRate mediaRate : mediaRates2) {
    //             totalRating2 += mediaRate.getRating();
    //         }
//
    //         return Double.compare(totalRating2/mediaRates2.size(), totalRating1/mediaRates.size());
    //     }).flatMap(media -> {
    //         List<MediaRate> mediaRates = mediasRateByUser.getOrDefault(media.getId(), new ArrayList<>());
//
    //         double totalRating = 0;
//
    //         for (MediaRate mediaRate : mediaRates) {
    //             totalRating += mediaRate.getRating();
    //         }
//
    //         return Mono.just(media.getTitle() + " - " + totalRating/mediaRates.size());
    //     }));
    // }
}

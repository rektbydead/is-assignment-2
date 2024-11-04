package client.exercices;

import client.Client;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.model.Media;

import java.time.LocalDate;
import java.util.DoubleSummaryStatistics;
import java.util.concurrent.Callable;

public class Exercice6 implements Callable<Flux<Object>> {

    private final WebClient webClient;
    public Exercice6(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<Object> call() throws Exception {
        return webClient.get()
                .uri("/media/")
                .retrieve()
                .bodyToFlux(Media.class)
                .map(Media::getAverageRating)
                .reduce(new double[]{0, 0, 0}, (value, rating) -> {
                    value[0] += rating;               // Sum of ratings
                    value[1] += rating * rating;      // Sum of squared ratings
                    value[2] += 1;                    // Count of ratings
                    return value;
                })
                .flatMapMany(acc -> {
                    if (acc[2] == 0) {
                        return Flux.just("No ratings available.");
                    }

                    double mean = acc[0] / acc[2];
                    double variance = (acc[1] / acc[2]) - (mean * mean);
                    double stdDev = Math.sqrt(variance);
                    return Flux.just(String.format("Average rating: %.2f, Standard deviation: %.2f", mean, stdDev));
                });
    }
}

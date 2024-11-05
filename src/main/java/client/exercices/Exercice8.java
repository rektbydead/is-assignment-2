package client.exercices;

import client.Client;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import uc2024135137.is.tp2.model.Media;

import java.util.concurrent.Callable;

public class Exercice8 implements Callable<Flux<?>> {

    private final WebClient webClient;
    public Exercice8(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<Double> call() throws Exception {
        return webClient.get()
                .uri("/media/")
                .retrieve()
                .bodyToFlux(Media.class)
                .reduce(new int[]{0, 0}, (value, media) -> {
                    value[0] += media.getNumberOfRates();
                    value[1] += 1;
                    return value;
                }).flatMapMany((value) -> {
                    double average = value[1] > 0 ? (double) value[0] / value[1] : 0;
                    return Flux.just(average);
                });
    }
}

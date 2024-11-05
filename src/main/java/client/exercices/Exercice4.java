package client.exercices;

import client.Client;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import uc2024135137.is.tp2.model.Media;

import java.time.LocalDate;
import java.util.concurrent.Callable;

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
}

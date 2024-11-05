package client.exercices;

import client.Client;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.model.Media;

import java.util.Comparator;
import java.util.concurrent.Callable;

public class Exercice7 implements Callable<Flux<?>> {

    private final WebClient webClient;
    public Exercice7(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<Media> call() throws Exception {
        return webClient.get()
                .uri("/media/")
                .retrieve()
                .bodyToFlux(Media.class)
                .sort(Comparator.comparing(Media::getReleaseDate))
                .take(1);
    }
}

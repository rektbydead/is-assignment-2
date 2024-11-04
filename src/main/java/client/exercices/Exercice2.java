package client.exercices;

import client.Client;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc2024135137.is.tp2.model.Media;

import java.util.concurrent.Callable;

public class Exercice2 implements Callable<Flux<Object>> {

    private final WebClient webClient;
    public Exercice2(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<Object> call() throws Exception {
        return webClient.get()
                .uri("/media/")
                .retrieve()
                .bodyToFlux(Media.class)
                .count().flatMapMany(Flux::just);
    }
}

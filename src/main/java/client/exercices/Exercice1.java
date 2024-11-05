package client.exercices;

import client.Client;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import uc2024135137.is.tp2.model.Media;
import uc2024135137.is.tp2.model.MediaRate;

import java.time.Duration;
import java.util.concurrent.Callable;

public class Exercice1 implements Callable<Flux<?>> {

    private final WebClient webClient;
    public Exercice1(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<String> call() throws Exception {
        return webClient.get()
                .uri("/media/")
                .retrieve()
                .bodyToFlux(Media.class)
                .retryWhen(Retry
                        .fixedDelay(3, Duration.ofSeconds(1))
                        .doBeforeRetry(retrySignal -> System.out.println("ExerciseError1: Retrying due to: " + retrySignal.failure().getMessage()))
                )
                .flatMap(media->
                        Mono.just("Title: " + media.getTitle() + "; Release date: " + media.getReleaseDate())
                );
    }
}

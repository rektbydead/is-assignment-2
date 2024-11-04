package client.exercices;

import client.Client;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.Callable;

public class ExerciceError1 implements Callable<Flux<Object>> {

    private final WebClient webClient;
    public ExerciceError1(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<Object> call() throws Exception {
        return webClient.get()
                .uri("/testerror/1")
                .retrieve()
                .bodyToFlux(Object.class)
                .retryWhen(Retry.max(3)
                        .doBeforeRetry(retrySignal -> System.out.println("ExerciseError1: Retrying due to: " + retrySignal.failure().getMessage()))
                );
    }
}

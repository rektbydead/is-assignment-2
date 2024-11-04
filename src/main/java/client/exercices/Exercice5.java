package client.exercices;

import client.Client;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import uc2024135137.is.tp2.model.Media;

import java.time.LocalDate;
import java.util.concurrent.Callable;

public class Exercice5 implements Callable<Flux<Object>> {

    private final WebClient webClient;
    public Exercice5(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<Object> call() throws Exception {
        return webClient.get()
                .uri("/media/")
                .retrieve()
                .bodyToFlux(Media.class)
                .filter(media -> {
                    LocalDate firstDate = LocalDate.of(1980, 1, 1);
                    LocalDate lastDate = LocalDate.of(1989, 12, 31);
                    LocalDate releaseDate = media.getReleaseDate();
                    return releaseDate.isAfter(firstDate) && releaseDate.isBefore(lastDate);
                }).sort((media1, media2) -> Double.compare(media2.getAverageRating(), media1.getAverageRating())).flatMap(Flux::just);
    }
}

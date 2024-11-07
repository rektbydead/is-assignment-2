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
                .map((media) ->
                    String.format("(%d) %s (%s) - %.2f", media.getId(), media.getTitle(), media.getReleaseDate().toString(), media.getAverageRating())
                );
    }
}

package client;

import client.exercices.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Client {

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        Thread.sleep(1000);

        String filename = args.length >= 1 ? args[0] : "output.txt";

        WebClient WEB_CLIENT = WebClient.builder()
                .baseUrl("http://127.0.0.1:9999")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        List<String> questions = List.of("1.Titles\tand\trelease\tdates\tof\tall\tmedia\titems.",
                "2. Total\tcount\tof\tmedia\titems.", "3. Total\tcount\tof\tmedia\titems\t that\tare\treally\tgood\t (i.e.,\t that\thave\tmore\t than\t8\ton\t\n" +
                        "average\trating).", "4. Total\tcount\tof\tmedia\titems\tthat\tare\tsubscribed.",
                "5. Data\tof\tmedia\titems\tthat\tare\tfrom\tthe\t80’s\t(i.e.,\twhose\trelease\tdates\tare\tbetween\t\n" +
                        "01-01-1980\tand\t31-12-1989).\tThis\tlist\tshould\tbe\tsorted\tso\tthat\tmedia\titems\twith\t\n" +
                        "greater\taverage\tratings\tcome\tfirst.\tThis\tdata does not\tneed\tto\tinclude\tthe\tusers.", "6. Average\tand\tstandard\tdeviations\tof\tall\tmedia\titems\tratings.",
                "7. The\tname\tof\tthe\toldest\tmedia\titem.", "8. The\taverage\tnumber\tof\tusers\tper\tmedia\titem.\tNote\tthat\tsome\tmedia\titems\tmay\t\n" +
                        "not\thave\tusers\tand\tvice\tversa.", "9. Name\t and\t number\t of\t users\t per\tmedia\titem,\t sorted\t by\t user\t age\tin\t descending\t\n" +
                        "order.", "10. Complete\tdata\tof\tall\tusers,\tby\tadding\tthe\tnames\tof\tsubscribed\tmedia\titems.", "11. Exercise error -> Retry 3 times", "11. Exercise error -> Retry 3 times with maximom of 15s wait");


        List<Callable<Flux<Object>>> runnables = new ArrayList<>(Arrays.asList(
                new Exercice1(WEB_CLIENT),
                new Exercice2(WEB_CLIENT),
                new Exercice3(WEB_CLIENT),
                new Exercice4(WEB_CLIENT),
                new Exercice5(WEB_CLIENT),
                new Exercice6(WEB_CLIENT),
                new Exercice7(WEB_CLIENT),
                new Exercice8(WEB_CLIENT),
                new Exercice9(WEB_CLIENT),
                new Exercice10(WEB_CLIENT)
                // new ExerciceError1()
        ));


        PrintWriter writer = new PrintWriter(filename, StandardCharsets.UTF_8);
        CountDownLatch countDownLatch = new CountDownLatch(runnables.size());
        ExecutorService executor = Executors.newFixedThreadPool(10);

        System.out.println("Starting now.");
        long start = System.currentTimeMillis();
        List<Future<Flux<Object>>> fluxes = executor.invokeAll(runnables);

        for (int i = 0; i < fluxes.size(); i++) {
            Future<Flux<Object>> future = fluxes.get(i);
            writer.println(questions.get(i));
            CountDownLatch futureCountDownLatch = new CountDownLatch(1);
            future.get().doOnError(e -> {
                writer.println("Could not connect to server.");
                futureCountDownLatch.countDown();
            }).doOnComplete(futureCountDownLatch::countDown).subscribe(writer::println);
            futureCountDownLatch.await();
            countDownLatch.countDown();
        }

        System.out.println("Just ended: " + (System.currentTimeMillis() - start) + " ms");

        countDownLatch.await();
        writer.close();
        executor.shutdown();
    }
}

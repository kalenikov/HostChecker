package ru.kalenikov;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;


public class CheckServUtils {
    private static String SERVER_ADDRESS = "devsygma8.sigma-it.local";
    private static int TCP_SERVER_PORT = 8080;
    static String LOGIN_BTN = "btn btn-block btn-login";

    static final Logger logger = LoggerFactory.getLogger(CheckServUtils.class);

    public static void hostAvailabilityCheck2() throws InterruptedException, IOException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create("http://devsygma8.sigma-it.local:8080"))
                .uri(URI.create("http://devsygma8.sigma-it.local:8080"))
                .timeout(Duration.of(1, SECONDS))
                .build();

        while (true) {
            boolean isAvailable = false;
            try {
                HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
                isAvailable = response.body().indexOf(LOGIN_BTN) > 0;
            } catch (IOException ex1) {
//                System.out.println(ex.getMessage());
            }
            Thread.sleep(1000L);
            logger.info(String.valueOf(isAvailable));
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        CheckServUtils.hostAvailabilityCheck2();
    }

//
//    public static void main(String[] args) throws InterruptedException, IOException {
//        CheckServUtils.hostAvailabilityCheck2();
//    }

}

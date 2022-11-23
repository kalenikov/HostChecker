package ru.kalenikov;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
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

@SuppressWarnings("InfiniteLoopStatement")
public class Main {
    private static String SERVER_ADDRESS = "devsygma8.sigma-it.local";
    private static int TCP_SERVER_PORT = 8080;
    private static String URL = "http://devsygma8.sigma-it.local:8080";
    static String LOGIN_BTN = "btn btn-block btn-login";

    static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void hostAvailabilityCheck2() throws InterruptedException, IOException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(URI.create(URL))
                .timeout(Duration.of(1, SECONDS))
                .build();

        boolean lastResult = false;
        boolean currentResult = false;
        while (true) {
            try {
                HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
                currentResult = response.body().indexOf(LOGIN_BTN) > 0;
            } catch (IOException ignored) {
                currentResult = false;
            }
            Thread.sleep(1000L);
            logger.info(String.valueOf(currentResult));
            if (currentResult != lastResult) {
                sendNotify(currentResult);
                lastResult = currentResult;
            }
        }
    }

    // если сервер поднялся, то прислать одно уведомление
    // и прислать опять, если сервер упал

    public static void main(String[] args) throws IOException, InterruptedException {
        hostAvailabilityCheck2();
    }

    private static void sendNotify(Boolean serverIsUp) {
        String chatId = ApplicationProperties.INSTANCE.get("telegram.admin");
        String token = ApplicationProperties.INSTANCE.get("telegram.bot.token");
        TelegramBot bot = new TelegramBot(token);
        String text = serverIsUp? "server is up": "server is down";
        bot.execute(new SendMessage(chatId, text));
    }
}

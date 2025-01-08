package com.payments.payments_back.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payments.payments_back.exceptions.BinlistNotFoundException;
import com.payments.payments_back.exceptions.BinlistTooManyException;
import com.payments.payments_back.model.CardInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class BinlistClient {

    public static final String HEADER_ACCEPT_VERSION = "Accept-Version";
    public static final String HEADER_ACCEPT_VERSION_VALUE = "3";
    /**
     * HttpClient is thread-safe and can be reused for multiple requests,
     * offering efficient and high-performance HTTP communication
     */
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ExecutorService executorService;
    private final String binlistUrl;

    public BinlistClient(HttpClient httpClient,
                         ObjectMapper objectMapper,
                         ExecutorService executorService,
                         @Value("${binlinst.url}") String binlistUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.executorService = executorService;
        this.binlistUrl = binlistUrl;
    }

    /**
     * Synchronous method to get card country info using binlist API
     */
    public String getCardCountry(String cardNumber) throws Exception {
        Callable<String> task = () -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(String.format(binlistUrl, cardNumber)))
                    .header(HEADER_ACCEPT_VERSION, HEADER_ACCEPT_VERSION_VALUE)
                    .timeout(Duration.ofMillis(10000)) // Set timeout
                    .build();

            // Send the request and get the response synchronously
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpStatus.OK.value()) {
                if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
                    throw new BinlistNotFoundException("Binlist Card Country not found");
                } else if (response.statusCode() == HttpStatus.TOO_MANY_REQUESTS.value()) {
                    throw new BinlistTooManyException("Too Many hits");
                } else if (response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                    throw new RuntimeException("Internal Server Error");
                } else {
                    throw new RuntimeException("Failed to retrieve card info: " + response.statusCode());
                }
            }

            CardInfoResponse cardInfoResponse = objectMapper.readValue(response.body(), CardInfoResponse.class);
            return cardInfoResponse.country().alpha2();
        };

        Future<String> future = executorService.submit(task);
        return future.get();
    }
}
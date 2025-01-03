package com.payments.payments_back.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payments.payments_back.exceptions.BinlistNotFoundException;
import com.payments.payments_back.exceptions.BinlistTooManyException;
import com.payments.payments_back.model.CardInfoResponse;
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

    /**
     * HttpClient is thread-safe and can be reused for multiple requests,
     * offering efficient and high-performance HTTP communication
     */
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ExecutorService executorService;

    public BinlistClient(HttpClient httpClient, ObjectMapper objectMapper, ExecutorService executorService) {
        // Initialize HttpClient (which is thread-safe and can be reused for multiple requests)
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.executorService = executorService;
    }

    /**
     * Synchronous method to get card country info using binlist API
     */
    public String getCardCountry(String cardNumber) throws Exception {
        // Create and return the result from a virtual thread
        Callable<String> task = () -> {
            try {
                // Create the HTTP request
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://lookup.binlist.net/" + cardNumber))
                        .header("Accept-Version", "3")
                        .timeout(Duration.ofMillis(10000)) // Set timeout
                        .build();

                // Send the request and get the response synchronously
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                // Handle errors (non-2xx responses)
                if (response.statusCode() != 200) {
                    if (response.statusCode() == 404) {
                        throw new BinlistNotFoundException("Binlist Card Country not found");
                    } else if (response.statusCode() == 429) {
                        throw new BinlistTooManyException("Too Many hits");
                    } else if (response.statusCode() == 500) {
                        throw new RuntimeException("Internal Server Error");
                    } else {
                        throw new RuntimeException("Failed to retrieve card info: " + response.statusCode());
                    }
                }

                // Parse the JSON response to extract the country
                CardInfoResponse cardInfoResponse = objectMapper.readValue(response.body(), CardInfoResponse.class);

                // Return the country from the CardInfoResponse object
                return cardInfoResponse.country().alpha2();

            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Error during card lookup: " + ex.getMessage(), ex); // Proper error handling
            }
        };

        Future<String> future = executorService.submit(task);
        return future.get();
    }
}
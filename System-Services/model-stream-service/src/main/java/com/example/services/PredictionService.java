package com.example.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final WebClient.Builder webClientBuilder;
    //@TimeLimiter(name = "model-service")
    //@Retry(name = "model-service")

    @CircuitBreaker(name="model-service",fallbackMethod = "fallbackMethod")
    public String getPrediction(String requestData) {
        String ret = "";
        // Send the POST request to Model Service
        String responseMono = webClientBuilder.baseUrl("http://ML-MODEL-SERVICE")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build()
                .post()
                .uri("/model/predict")
                .body(BodyInserters.fromValue(requestData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        ret = responseMono;
        return ret;
    }
    public String fallbackMethod(String requestData,Exception e){
        return "Something went wrong, please try again later!";
    }


//    System.out.println("BKHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
    //return CompletableFuture.supplyAsync(() ->"Something went wrong, please try again later!");
}

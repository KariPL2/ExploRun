package com.example.ExploRun.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public class OpenRouteServiceConnector{

  private final WebClient webClient;

  @Value("${openrouteservice.api.key}")
  private String apiKey;

  public OpenRouteServiceConnector(WebClient.Builder webClientBuilder,
                                   @Value("${openrouteservice.api.url}") String baseUrl) {
    this.webClient = webClientBuilder.baseUrl(baseUrl).build();
  }

  public String generateRoute(List<double[]> coordinates) {


  }
}

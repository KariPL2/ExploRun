package com.example.ExploRun.config;


import com.example.ExploRun.dto.Point;
import com.example.ExploRun.request.OpenRouteServiceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class OpenRouteServiceConnector{

  private final WebClient webClient;
  private final ObjectMapper objectMapper;
  private final String apiKey;

  public OpenRouteServiceConnector(
      WebClient.Builder webClientBuilder,
      ObjectMapper objectMapper,
      @Value("${openrouteservice.api.url}") String baseUrl,
      @Value("${openrouteservice.api.key}") String apiKey)
  {
    this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    this.objectMapper = objectMapper;
    this.apiKey = apiKey;
  }

/*  public String generateRoute(List<Point> waypoints, boolean isRound) {
    OpenRouteServiceRequest openRouteServiceRequest = new OpenRouteServiceRequest();


  }*/
}

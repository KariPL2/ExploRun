package com.example.ExploRun.config;


import com.example.ExploRun.dto.Point;
import com.example.ExploRun.request.OpenRouteServiceRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

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
    this.webClient = webClientBuilder.baseUrl(baseUrl + "/v2/directions").build();
    this.objectMapper = objectMapper;
    this.apiKey = apiKey;
  }

  public String generateRoute(List<Point> waypoints, boolean isRound) throws JsonProcessingException {
    OpenRouteServiceRequest requestBodyObject;

    if(isRound){
      waypoints.add(waypoints.getFirst());
      requestBodyObject = OpenRouteServiceRequest.builder()
          .coordinates(waypoints)
          .profile("foot-walking")
          .preference("recommended")
          .build();
    }else{
      requestBodyObject = OpenRouteServiceRequest.builder()
          .coordinates(waypoints)
          .profile("foot-walking")
          .preference("recommended")
          .build();
    }
    String requestBody = objectMapper.writeValueAsString(requestBodyObject);

    String profilePath = requestBodyObject.getProfile();

    return webClient.post()
        .uri(uriBuilder -> uriBuilder.pathSegment(profilePath).build())
        .header("Accept", "application/json, application/geo+json")
        .header("Authorization", apiKey)
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(String.class)
        .block();
  }
}

package com.example.ExploRun.config;


import com.example.ExploRun.entity.Point;
import com.example.ExploRun.request.OpenRouteServiceRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class OpenRouteServiceClient{

  private final WebClient webClient;
  private final String apiKey;

  public OpenRouteServiceClient(
      WebClient.Builder webClientBuilder,
      @Value("${openrouteservice.api.url}") String baseUrl,
      @Value("${openrouteservice.api.key}") String apiKey)
  {
    this.webClient = webClientBuilder.baseUrl(baseUrl + "/v2/directions").build();
    this.apiKey = apiKey;
  }

  public String generateRouteJson(List<Point> coordinates, boolean isRound,String profile, String preference) {
    OpenRouteServiceRequest requestBodyObject;

    if(isRound){
      coordinates.add(coordinates.getFirst());
      requestBodyObject = OpenRouteServiceRequest.builder()
          .coordinates(coordinates)
          .profile(profile)
          .preference(preference)
          .build();
    }else{
      requestBodyObject = OpenRouteServiceRequest.builder()
          .coordinates(coordinates)
          .profile(profile)
          .preference(preference)
          .build();
    }
    String profilePath = requestBodyObject.getProfile();

    return webClient.post()
        .uri(uriBuilder -> uriBuilder.pathSegment(profilePath).build())
        .header("Accept", "application/json, application/geo+json")
        .header("Authorization", apiKey)
        .bodyValue(requestBodyObject)
        .retrieve()
        .bodyToMono(String.class)
        .block();
  }
}

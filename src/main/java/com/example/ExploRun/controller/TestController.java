package com.example.ExploRun.controller;


import com.example.ExploRun.config.OpenRouteServiceClient;
import com.example.ExploRun.request.OpenRouteServiceRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestController {

  private final OpenRouteServiceClient openRouteServiceClient;

  public TestController(OpenRouteServiceClient openRouteServiceClient) {
    this.openRouteServiceClient = openRouteServiceClient;
  }

//  @GetMapping
//  @PreAuthorize("hasAuthority('VIEW_ALL_ROUTES')")
//  public ResponseEntity<List<>> getAllUsers() {}

  @PostMapping
  @PreAuthorize("hasAuthority('PLAN_ROUTE_BASIC')")
  public ResponseEntity<String> test(@RequestBody OpenRouteServiceRequest request) {
    String route = openRouteServiceClient.generateRouteJson(request.getCoordinates(), false, request.getProfile(),  request.getPreference());
    return ResponseEntity.ok(route);
  }


}

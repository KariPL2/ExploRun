package com.example.ExploRun.service.impl;

import com.example.ExploRun.config.OpenRouteServiceClient;
import com.example.ExploRun.dto.OrsResponseDTO;
import com.example.ExploRun.dto.RouteDTO;
import com.example.ExploRun.dto.UserDTO;
import com.example.ExploRun.entity.Point;
import com.example.ExploRun.entity.Route;
import com.example.ExploRun.entity.User;
import com.example.ExploRun.mapper.RouteMapper;
import com.example.ExploRun.repository.RouteRepository;
import com.example.ExploRun.service.RouteService;
import com.example.ExploRun.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

  private final OpenRouteServiceClient orsClient;
  private final RouteRepository routeRepository;
  private final UserService userService; // Załóżmy, że masz takie repozytorium
  private final RouteMapper routeMapper;
  private final ObjectMapper objectMapper; // Spring Boot dostarcza tę fasolkę automatycznie

  @Override
  @Transactional // Zapewnia, że cała operacja zapisu (trasa + segmenty + kroki) jest atomowa
  @SneakyThrows // Upraszcza obsługę wyjątków z ObjectMapper (w praktyce warto mieć lepszą obsługę)
  public Route createAndSaveRoute(String username, List<Point> coordinates, boolean isRound, String profile, String preference) {
    // 1. Znajdź użytkownika, do którego przypiszesz trasę
    User user = userService.findByUsername(username);
    // 2. Pobierz surowy JSON z OpenRouteService
    String routeJson = orsClient.generateRouteJson(coordinates, isRound, profile, preference);

    // 3. Zmapuj JSON na nasze DTO
    OrsResponseDTO orsResponse = objectMapper.readValue(routeJson, OrsResponseDTO.class);

    if (orsResponse.getRoutes() == null || orsResponse.getRoutes().isEmpty()) {
      throw new RuntimeException();//RouteNotFoundException("Could not generate a route for the given coordinates.");
    }
    RouteDTO routeDto = orsResponse.getRoutes().getFirst();

    Route routeEntity = routeMapper.DTOToEntity(routeDto);

    routeEntity.setUser(user);
    routeEntity.setOriginalJson(routeJson);
    routeEntity.setProfile(profile);
    routeEntity.setPreference(preference);
    routeEntity.setCoordinates(coordinates);

    return routeRepository.save(routeEntity);
  }
  @Override
  public List<Route> findAllRoutes() {
    return routeRepository.findAll();
  }

  @Override
  public List<Route> findRoutesByUserId(UUID userId) {
    return routeRepository.findByUserId(userId);
  }

  @Override
  public List<Route> findRoutesByUserUsername(String username) {
    return routeRepository.findAllByUserUsername(username);
  }
}

package com.example.ExploRun.controller;

import com.example.ExploRun.dto.RouteDTO;
import com.example.ExploRun.entity.Route;
import com.example.ExploRun.mapper.RouteMapper;
import com.example.ExploRun.request.OpenRouteServiceRequest;
import com.example.ExploRun.service.RouteService;
import com.example.ExploRun.userDetails.CustomUserDetails;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

  private final RouteService routeService;
  private final RouteMapper routeMapper;

  @GetMapping("/me/routes")
  @PreAuthorize("hasAuthority('VIEW_OWN_ROUTES')")
  public ResponseEntity<List<RouteDTO>> getMyRoutes(Authentication authentication) {
    // Pobieramy ID użytkownika bezpośrednio z obiektu Authentication
    UserDetails principal = (CustomUserDetails) authentication.getPrincipal();
    String username = principal.getUsername();

    List<Route> routes = routeService.findRoutesByUserUsername(username);
    List<RouteDTO> routeDTOs = routes.stream()
        .map(routeMapper::EntityToDTO)
        .collect(Collectors.toList());
    return ResponseEntity.ok(routeDTOs);
  }

  // --- NOWY, LEPSZY ENDPOINT DO TWORZENIA TRASY DLA SIEBIE ---
  @PostMapping("/me/routes")
  @PreAuthorize("hasAuthority('SAVE_ROUTE')")
  public ResponseEntity<RouteDTO> createMyRoute(
      @RequestBody OpenRouteServiceRequest request,
      Authentication authentication) {
    CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
    String username = principal.getUsername();

    Route savedRoute = routeService.createAndSaveRoute(
        username,
        request.getCoordinates(),
        request.isRound(),
        request.getProfile(),
        request.getPreference()
    );

    RouteDTO responseDto = routeMapper.EntityToDTO(savedRoute);
    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }
  @GetMapping
  @PreAuthorize("hasAuthority('VIEW_ALL_ROUTES')")
  public ResponseEntity<List<RouteDTO>> getAllRoutes() {
    List<Route> routes = routeService.findAllRoutes();
    List<RouteDTO> routeDTOs = routes.stream()
        .map(routeMapper::EntityToDTO)
        .collect(Collectors.toList());
    return ResponseEntity.ok(routeDTOs);
  }

  @GetMapping("/users/{userId}")
  @PreAuthorize("hasAuthority('VIEW_OWN_ROUTES') and (#userId == authentication.principal.id or hasAuthority('VIEW_ALL_ROUTES'))")
  public ResponseEntity<List<RouteDTO>> getRoutesByUser(@PathVariable UUID userId) {
    List<Route> routes = routeService.findRoutesByUserId(userId);
    List<RouteDTO> routeDTOs = routes.stream()
        .map(routeMapper::EntityToDTO)
        .collect(Collectors.toList());
    return ResponseEntity.ok(routeDTOs);
  }
}

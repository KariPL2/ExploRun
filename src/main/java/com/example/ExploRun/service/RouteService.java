package com.example.ExploRun.service;

import com.example.ExploRun.entity.Point;
import com.example.ExploRun.entity.Route;

import java.util.List;
import java.util.UUID;

public interface RouteService {

  Route createAndSaveRoute(String username, List<Point> coordinates, boolean isRound, String profile, String preference);
  List<Route> findAllRoutes();
  List<Route> findRoutesByUserId(UUID userId);
  List<Route> findRoutesByUserUsername(String username);
}

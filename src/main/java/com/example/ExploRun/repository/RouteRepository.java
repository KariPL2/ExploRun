package com.example.ExploRun.repository;

import com.example.ExploRun.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RouteRepository extends JpaRepository<Route, Long> {

  List<Route> findByUserId(UUID userId);
  List <Route> findAllByUserUsername(String username);
}

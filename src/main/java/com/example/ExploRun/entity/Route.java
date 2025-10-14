package com.example.ExploRun.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Route {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ElementCollection
  @CollectionTable(
      name = "route_waypoints",
      joinColumns = @JoinColumn(name = "route_id")
  )
  private List<Point> coordinates;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false) // Klucz obcy w tabeli ROUTE
  private User user;

  private String profile; // np. "foot-walking"
  private String preference; // np. "recommended"

  private double totalDistance; // w metrach
  private double totalDuration; // w sekundach

  @Column(columnDefinition = "TEXT")
  private String geometryPolyline; // Zakodowana geometria trasy

  @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Segment> segments;

  @Column(columnDefinition = "TEXT")
  private String originalJson;



}

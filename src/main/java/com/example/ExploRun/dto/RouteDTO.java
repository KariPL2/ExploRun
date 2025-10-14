package com.example.ExploRun.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class RouteDTO {
  // Odzwierciedla pole "summary"
  private SummaryDTO summary;

  // Odzwierciedla pole "segments"
  private List<SegmentDTO> segments;

  // Odzwierciedla pole "geometry" (zakodowany polyline)
  private String geometry;

  // Użyj JsonProperty, aby dopasować się do nazwy w JSON
  @JsonProperty("way_points")
  private List<Integer> wayPoints;
}
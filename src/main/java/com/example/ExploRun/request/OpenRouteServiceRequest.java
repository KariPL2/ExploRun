package com.example.ExploRun.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class OpenRouteServiceRequest {


  /**
   *The waypoints to use for the route as an array of
   *longitude/latitude pairs in WGS 84 (EPSG:4326)
   */
  @JsonProperty("coordinates")
  List<double[]> coordinates;

  /**
   * Specifies the route profile.
   * Possible values:
   * driving-car
   * driving-hgv
   * cycling-regular
   * cycling-road
   * cycling-mountain
   * cycling-electric
   * foot-walking
   * foot-hiking
   * wheelchair
   */
  @JsonProperty("profile")
  private String profile;

  /**
   * Specifies the route preference:
   * fastest
   * shortest
   * recommended
   * custom
   */
  @JsonProperty("preference")
  private String preference;

  /**
   * Advanced options for routing
   * avoid_features : ["highways", "tollways", "ferries"]
   * avoid_countries : [...]
   * avoid_polygons : [...]
   * round_trip : [ {
   *  length : 10000(meters),
   *  points : 2 (integer),
   *  seed : 1 (integer)
   * } ]
   */
  @JsonProperty("options")
  private Map<String, Object> options;

}

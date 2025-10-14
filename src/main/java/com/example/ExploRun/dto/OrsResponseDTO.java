package com.example.ExploRun.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrsResponseDTO {
  private List<RouteDTO> routes;
}
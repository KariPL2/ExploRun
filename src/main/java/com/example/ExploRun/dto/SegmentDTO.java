package com.example.ExploRun.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SegmentDTO {
  private double distance;
  private double duration;
  private List<StepDTO> steps;
}

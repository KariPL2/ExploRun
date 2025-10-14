package com.example.ExploRun.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StepDTO {
  private double distance;
  private double duration;
  private int type;
  private String instruction;
  private String name; // Nazwa ulicy
}

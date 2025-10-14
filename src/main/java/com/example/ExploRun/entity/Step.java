package com.example.ExploRun.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Step {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private double distance; // Dystans do następnego punktu
  private double duration; // Czas do następnego punktu
  private int type;

  @Column(columnDefinition = "VARCHAR(255)")
  private String instruction;

  @Column(columnDefinition = "VARCHAR(100)")
  private String streetName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "segment_id", nullable = false)
  private Segment segment;
}
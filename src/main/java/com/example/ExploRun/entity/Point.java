package com.example.ExploRun.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Embeddable;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@Embeddable
public record Point(double lng, double lat) {
}

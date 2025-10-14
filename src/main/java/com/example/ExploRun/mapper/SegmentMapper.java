package com.example.ExploRun.mapper;

import com.example.ExploRun.dto.SegmentDTO;
import com.example.ExploRun.entity.Segment;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = StepMapper.class)
public abstract class SegmentMapper {

  public abstract SegmentDTO SegmentToSegmentDTO(Segment segment);

  public abstract Segment DTOToEntity(SegmentDTO dto);

  @AfterMapping
  protected void afterMapping(SegmentDTO dto, @MappingTarget Segment segment) {
    if (segment.getSteps() != null) {
      segment.getSteps().forEach(step -> step.setSegment(segment));
    }
  }
}

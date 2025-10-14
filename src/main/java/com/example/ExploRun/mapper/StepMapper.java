package com.example.ExploRun.mapper;

import com.example.ExploRun.dto.StepDTO;
import com.example.ExploRun.entity.Step;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class StepMapper {
  @Mapping(source = "name", target = "streetName")
  public abstract Step DTOToEntity(StepDTO dto);

  public abstract StepDTO StepToStepDTO(Step step);

}

package com.example.ExploRun.mapper;

import com.example.ExploRun.dto.RouteDTO;
import com.example.ExploRun.dto.SummaryDTO;
import com.example.ExploRun.entity.Route;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {SegmentMapper.class})
public abstract class RouteMapper {


  @Mapping(source = "geometryPolyline", target = "geometry")
  @Mapping(target = "summary", ignore = true)
  @Mapping(target = "wayPoints", ignore = true)
  public abstract RouteDTO EntityToDTO(Route route);

  @AfterMapping
  public void afterEntityToDTO(Route route, @MappingTarget RouteDTO routeDTO) {
    SummaryDTO summaryDTO = new SummaryDTO();
    summaryDTO.setDistance(route.getTotalDistance());
    summaryDTO.setDuration(route.getTotalDuration());
    routeDTO.setSummary(summaryDTO);
  }


  @Mapping(source = "summary.distance", target = "totalDistance")
  @Mapping(source = "summary.duration", target = "totalDuration")
  @Mapping(source = "geometry", target = "geometryPolyline")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "originalJson", ignore = true)
  @Mapping(target = "coordinates", ignore = true)
  public abstract Route DTOToEntity(RouteDTO dto);

  @AfterMapping
  protected void afterDTOToEntity(RouteDTO dto, @MappingTarget Route route) {
    if (route.getSegments() != null) {
      route.getSegments().forEach(segment -> segment.setRoute(route));
    }
  }


}

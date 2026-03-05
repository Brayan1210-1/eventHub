package com.cesde.eventhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.cesde.eventhub.dto.request.EventRegisterDTO;
import com.cesde.eventhub.dto.response.EventResponseDTO;
import com.cesde.eventhub.entity.Event;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

   
    Event toEntity(EventRegisterDTO dto);

    @Mapping(source = "zone.name", target = "zoneName")
    @Mapping(source = "zone.place.name", target = "placeName")
    @Mapping(source = "organizer.email", target = "organizerEmail")
    EventResponseDTO toDTO(Event entity);
}
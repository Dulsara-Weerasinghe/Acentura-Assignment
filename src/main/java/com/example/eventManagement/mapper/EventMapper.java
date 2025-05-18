package com.example.eventManagement.mapper;

import com.example.eventManagement.dto.ResponseBean;
import com.example.eventManagement.dto.UpdateEvent;
import com.example.eventManagement.entity.Attendance;
import com.example.eventManagement.entity.Event;
import com.sun.jdi.request.EventRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")

public interface EventMapper {


    String toResponse(String entity);

    Event toResponse(Event entity);

    void updateEntityFromRequest(UpdateEvent updateEvent, @MappingTarget Event entity);

}

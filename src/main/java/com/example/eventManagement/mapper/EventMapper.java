package com.example.eventManagement.mapper;

import com.example.eventManagement.dto.ResponseBean;
import com.example.eventManagement.entity.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {


    String toResponse(String entity);
}

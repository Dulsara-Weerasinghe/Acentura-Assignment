package com.example.eventManagement.domain;

import com.example.eventManagement.dto.EventFilterCriteria;
import com.example.eventManagement.entity.Event;

import java.util.List;

public interface EventFilterStrategy {
    List<Event> filter(List<Event> events, EventFilterCriteria criteria);
}

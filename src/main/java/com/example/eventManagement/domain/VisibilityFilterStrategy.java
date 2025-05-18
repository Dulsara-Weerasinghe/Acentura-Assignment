package com.example.eventManagement.domain;

import com.example.eventManagement.dto.EventFilterCriteria;
import com.example.eventManagement.entity.Event;

import java.util.List;

public class VisibilityFilterStrategy implements EventFilterStrategy {
    @Override
    public List<Event> filter(List<Event> events, EventFilterCriteria criteria) {
        return events.stream()
                .filter(event -> event.getVisibility() == criteria.getVisibility())
                .toList();
    }
}

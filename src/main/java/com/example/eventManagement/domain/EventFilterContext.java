package com.example.eventManagement.domain;

import com.example.eventManagement.dto.EventFilterCriteria;
import com.example.eventManagement.entity.Event;

import java.util.List;

public class EventFilterContext {
    private final List<EventFilterStrategy> strategies;

    public EventFilterContext(List<EventFilterStrategy> strategies) {
        this.strategies = strategies;
    }

    public List<Event> applyFilters(List<Event> events, EventFilterCriteria criteria) {
        List<Event> filteredEvents = events;
        for (EventFilterStrategy strategy : strategies) {
            filteredEvents = strategy.filter(filteredEvents, criteria);
        }
        return filteredEvents;
    }
}

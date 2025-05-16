package com.example.eventManagement.service;

import com.example.eventManagement.dto.*;
import com.example.eventManagement.exception.DataNotFounException;
import com.example.eventManagement.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;

public interface EventService {

    String createEvent(CreateEventRequest createEventRequest);

    ResponseBean updateTask(UpdateEvent updateEvent, String eventId) throws DataNotFounException;

    ResponseBean deleteEvent(String eventId);

    ResponseBean filterEvents(FilterEvents filterEvents);

    ResponseBean upcomingEvents(UpcomingEventsRequest upcomingEventsRequest);

    String checkEventId(String eventId) throws ResourceNotFoundException;

    Map<String, List<EventDto>> getUserHostedAndAttendingEvents(String userId);

    EventDetailsResponse getEventDetails(String id) throws ResourceNotFoundException;
}

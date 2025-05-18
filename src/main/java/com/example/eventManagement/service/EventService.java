package com.example.eventManagement.service;

import com.example.eventManagement.dto.*;
import com.example.eventManagement.exception.DataNotFounException;
import com.example.eventManagement.exception.NotFoundException;
import com.example.eventManagement.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;

public interface EventService {

    String createEvent(CreateEventRequest createEventRequest,String userName);

    ResponseBean updateTask(UpdateEvent updateEvent, String eventId,String userName) throws DataNotFounException, NotFoundException;

    ResponseBean deleteEvent(String eventId,String userName);

    ResponseBean filterEvents(EventFilterCriteria filterEvents);

    ResponseBean upcomingEvents(UpcomingEventsRequest upcomingEventsRequest);

    ResponseBean checkEventId(String eventId) throws ResourceNotFoundException;

    Map<String, List<EventDto>> getUserHostedAndAttendingEvents(String userId) throws NotFoundException;

    ResponseBean getEventDetails(String id) throws ResourceNotFoundException;


}

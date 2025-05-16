package com.example.eventManagement.controller;

import com.example.eventManagement.dto.*;
import com.example.eventManagement.exception.DataNotFounException;
import com.example.eventManagement.exception.ResourceNotFoundException;
import com.example.eventManagement.service.EventService;
import com.example.eventManagement.util.EndPoint;
import com.example.eventManagement.util.MessageVarList;
import com.example.eventManagement.util.StatusVarList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final MessageSource messageSource;

    private final MessageVarList messageVarList;
    private final EventService eventService;

    @PostMapping(value = EndPoint.CREATE_EVENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBean createEvent(Locale locale,
                                    @RequestBody CreateEventRequest requestBody) {
        try {
            String eventId = eventService.createEvent(requestBody);
            if (!eventId.isEmpty() && eventId != null) {
                return new ResponseBean(MessageVarList.RSP_SUCCESS, StatusVarList.SUCCESS,eventId);
            } else {
                return new ResponseBean(MessageVarList.RSP_NO_DATA_FOUND, StatusVarList.FAILED,null);
            }

        } catch (Exception e) {

            return new ResponseBean(MessageVarList.RSP_NOT_AUTHORISED,StatusVarList.EXCEPTION_OCCURED,null);
        }
    }

    @PutMapping(value = "/events/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    public ResponseBean updateEvent(@RequestBody UpdateEvent updateEvent, @PathVariable("eventId") String eventid) throws DataNotFounException {
        log.info("Updating a task {} ", updateEvent.toString());
        return eventService.updateTask(updateEvent, eventid);
    }


    @DeleteMapping(value = "/events/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('HOST', 'ADMIN')")
    public ResponseBean deleteEvent(@PathVariable("eventId") String eventId) throws DataNotFounException {
        log.info("Updating a task {} ", eventId.toString());
        return eventService.deleteEvent(eventId);
    }


    @PostMapping(value = EndPoint.FILTER_EVENT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBean filterEvents(@RequestBody FilterEvents filterEvents) {
        log.debug("Filtering event details " + filterEvents);

        return eventService.filterEvents(filterEvents);
    }


    @PostMapping(value = EndPoint.UPCOMING_EVENTS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBean filterEvents(@RequestBody UpcomingEventsRequest upcomingEventsRequest) {
        log.debug("Filtering event details " + upcomingEventsRequest);
        return eventService.upcomingEvents(upcomingEventsRequest);
    }

    @GetMapping(value = "/checkStatus/{eventId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public String checkStatus(@PathVariable ("eventId")String eventId) throws ResourceNotFoundException {
        log.debug("Filtering event details " + eventId);
        return eventService.checkEventId(eventId);
    }

    @GetMapping(value = "/users/{userId}/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<EventDto>>> getUserEvents(@PathVariable ("userId") String userId) {
        return ResponseEntity.ok(eventService.getUserHostedAndAttendingEvents(userId));
    }



    @GetMapping(value = "/events/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDetailsResponse> getEventDetails(@PathVariable String  eventId) throws ResourceNotFoundException {
        return ResponseEntity.ok(eventService.getEventDetails(eventId));
    }

}

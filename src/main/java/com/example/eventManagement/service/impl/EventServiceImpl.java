package com.example.eventManagement.service.impl;

import com.example.domain.*;
import com.example.eventManagement.domain.*;
import com.example.eventManagement.dto.*;
import com.example.eventManagement.entity.Event;
import com.example.eventManagement.entity.User;
import com.example.eventManagement.enums.EventVisibilityType;
import com.example.eventManagement.enums.UserRoleType;
import com.example.eventManagement.exception.DataNotFounException;
import com.example.eventManagement.exception.NotFoundException;
import com.example.eventManagement.exception.ResourceNotFoundException;
import com.example.eventManagement.mapper.EventMapper;
import com.example.eventManagement.repository.AttendenceRepository;
import com.example.eventManagement.repository.EventRepository;
import com.example.eventManagement.repository.UserRepository;
import com.example.eventManagement.service.EventService;
import com.example.eventManagement.util.ErrorDsc;
import com.example.eventManagement.util.MessageVarList;
import com.example.eventManagement.util.StatusVarList;
import com.example.eventManagement.util.Utility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor

public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;


    private final EventMapper eventMapper;

    private final AttendenceRepository attendenceRepository;

    private final UserRepository userRepository;


    @Override
    public String createEvent(CreateEventRequest createEventRequest, String userName) {
        try {

            User user = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
            Event event = Event.builder()
                    .title(createEventRequest.getTitle())
                    .description(createEventRequest.getDescription())
                    .hostId(user)
                    .startTime(createEventRequest.getStartTime())
                    .createdAt(LocalDateTime.now())
                    .eventId(Utility.generateEventId())
                    .archived(false)
                    .visibility(EventVisibilityType.valueOf(createEventRequest.getEventVisibilityType().name()))
                    .build();

            Event eventCreate = eventRepository.save(event);

            if (eventCreate != null) {

                return eventMapper.toResponse(eventCreate.getEventId());
            } else {
                return eventMapper.toResponse(eventCreate.getEventId());
            }

        } catch (Exception e) {

            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public User getByUserName(String userName) throws NotFoundException {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public ResponseBean updateTask(UpdateEvent updateEvent, String eventId, String userName) throws DataNotFounException, NotFoundException {
        log.debug("Event update request" + updateEvent);


        User requester = getByUserName(userName);
        Event task = eventRepository.findById(eventId).orElseThrow(() -> new DataNotFounException(ErrorDsc.ERR_DSC_TASK_NOT_FOUND));

        if (!task.getHostId().getId().equals(requester.getId()) && requester.getRole() != UserRoleType.ADMIN.name()) {
            throw new AccessDeniedException("Not authorized to update this event.");
        }

        eventMapper.updateEntityFromRequest(updateEvent, task);
        Event event = eventRepository.save(task);

        return new ResponseBean(MessageVarList.RSP_SUCCESS, StatusVarList.SUCCESSFULLY_UPDATED, event.getEventId());

    }

    @Override
    public ResponseBean deleteEvent(String eventId, String userName) {
        Optional<Event> event = eventRepository.findByEventId(eventId);

        Event events = event.get();
        events.setArchived(true); //SOFT DELETE MECHANISM
        Event save = eventRepository.save(events);

        return new ResponseBean(MessageVarList.RSP_SUCCESS, StatusVarList.SUCCESSFULLY_DELETED, save.getEventId());
    }

    @Override
    public ResponseBean filterEvents(EventFilterCriteria filterEvents) {

        List<Event> allEvents = (List<Event>) eventRepository.findAll();
        List<EventFilterStrategy> strategies = List.of(
                new DateRangeFilterStrategy(),
                new LocationFilterStrategy(),
                new VisibilityFilterStrategy()
        );

        EventFilterContext context = new EventFilterContext(strategies);

        List<Event> filteredEvents = context.applyFilters(allEvents, filterEvents);


        return new ResponseBean(MessageVarList.RSP_SUCCESS, StatusVarList.FETCHED_SUCCESS, filteredEvents);


    }

    @Override
    public ResponseBean upcomingEvents(UpcomingEventsRequest upcomingEventsRequest) {
        Pageable pageable = PageRequest.of(upcomingEventsRequest.getPage(), upcomingEventsRequest.getSize(), Sort.by("startTime").ascending());
        Page<Event> eventPage = eventRepository.findUpcomingEvents(LocalDateTime.now(), pageable);

        if (eventPage != null) {


            Page<Event> map = eventRepository.findUpcomingEvents(LocalDateTime.now(), pageable)
                    .map(eventMapper::toResponse);


            return new ResponseBean(MessageVarList.RSP_SUCCESS, StatusVarList.SUCCESS, map);
        } else {
            return new ResponseBean(MessageVarList.RSP_NO_DATA_FOUND, StatusVarList.FAILED, null);
        }
    }

    @Override
    public ResponseBean checkEventId(String eventId) throws ResourceNotFoundException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        LocalDateTime now = LocalDateTime.now();
        String status = (now.isBefore(event.getStartTime())) ? "UPCOMING"
                : (now.isAfter(event.getEndTime())) ? "ENDED"
                : "ONGOING";

        return new ResponseBean(MessageVarList.RSP_SUCCESS, StatusVarList.SUCCESS, status);
    }

    @Override
    public Map<String, List<EventDto>> getUserHostedAndAttendingEvents(String userId) throws NotFoundException {
        User user = userRepository.findByUserName(userId).orElseThrow(() -> new NotFoundException("User not found"));

        List<EventDto> hostedEvents = convertEventsToDtos(eventRepository.findByhostId(user));
        List<EventDto> attendingEvents = convertEventsToDtos(attendenceRepository.findEventsByAndCompositeEvent_UserId(user));

        return Map.of(
                "hosting", hostedEvents,
                "attending", attendingEvents
        );

    }

    @Override
    public ResponseBean getEventDetails(String id) throws ResourceNotFoundException {


        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        long attendeeCount = attendenceRepository.countAttendeesByEventId(id);
        EventDetailsResponse eventDetailsResponse = new EventDetailsResponse();
        eventDetailsResponse.setTitle(event.getTitle());
        eventDetailsResponse.setDescription(event.getDescription());
        eventDetailsResponse.setLocation(event.getLocation());
        eventDetailsResponse.setEndTime(event.getEndTime());
        eventDetailsResponse.setVisibility(event.getVisibility());
        eventDetailsResponse.setStartTime(event.getStartTime());
        eventDetailsResponse.setId(event.getEventId());
        eventDetailsResponse.setAttendeeCount(attendeeCount);

        return new ResponseBean(MessageVarList.RSP_SUCCESS, StatusVarList.SUCCESS, eventDetailsResponse);


    }


    public List<EventDto> convertEventsToDtos(List<Event> events) {
        return events.stream()
                .map(event -> new EventDto(
                        event.getEventId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getStartTime(),
                        event.getEndTime(),
                        event.getLocation(),
                        event.getVisibility()
                ))
                .toList();
    }


}

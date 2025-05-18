package com.example.eventManagement.test;

import com.example.eventManagement.dto.*;
import com.example.eventManagement.entity.Event;
import com.example.eventManagement.entity.User;
import com.example.eventManagement.enums.EventVisibilityType;
import com.example.eventManagement.enums.UserRoleType;
import com.example.eventManagement.exception.NotFoundException;
import com.example.eventManagement.exception.ResourceNotFoundException;
import com.example.eventManagement.mapper.EventMapper;
import com.example.eventManagement.repository.AttendenceRepository;
import com.example.eventManagement.repository.EventRepository;
import com.example.eventManagement.repository.UserRepository;
import com.example.eventManagement.service.EventService;
import com.example.eventManagement.service.impl.EventServiceImpl;
import com.example.eventManagement.util.MessageVarList;
import com.example.eventManagement.util.StatusVarList;
import com.example.eventManagement.util.Utility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventTestService {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AttendenceRepository attendanceRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventServiceImpl eventService;


    @Test
    void eventCreate() {
        System.out.println("✅ JUnit test is running!");
        // Arrange
        CreateEventRequest request = new CreateEventRequest();
        request.setTitle("Test Event");
        request.setDescription("A test description");
        request.setLocation("Test City");
        request.setStartTime(LocalDateTime.of(2025, 5, 20, 10, 0));
        request.setEndTime(LocalDateTime.of(2025, 5, 20, 12, 0));
        request.setEventVisibilityType(EventVisibilityType.PUBLIC);


        Event event = new Event();
        event.setEventId("fec61351-4789-4f3a-adac-ba5a506eda5d");
        event.setTitle(request.getTitle());

        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventMapper.toResponse("fec61351-4789-4f3a-adac-ba5a506eda5d")).thenReturn("Event Created Successfully");

        // Act
        String result = eventService.createEvent(request,"Username");

        // Assert
        assertEquals("Event Created Successfully", result);
        verify(eventRepository).save(any(Event.class));
        verify(eventMapper).toResponse("fec61351-4789-4f3a-adac-ba5a506eda5d");

        // Print result
        System.out.println("Returned Result: " + result);
    }


    @Test
    void updateTask_success() throws Exception {
        // Arrange
        String eventId = "abc123";
        String userName = "testuser";

        String userUUID = "fec61351-4789-4f3a-adac-ba5a506eda5d";

        UpdateEvent updateEvent = new UpdateEvent();
        updateEvent.setTitle("Updated Title");

        User user = new User();
        user.setId(userUUID);
        user.setName("Test User");
        user.setRole(UserRoleType.USER.name());

        Event event = new Event();
        event.setEventId(eventId);
        event.setHostId(user);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        // Simulate getByUserName
        doReturn(user).when(eventService).getByUserName(userName);  // Requires partial mock or reflection if not public

        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // Act
        ResponseBean response = eventService.updateTask(updateEvent, eventId, userName);

        // Assert
        assertNotNull(response);
        assertEquals(StatusVarList.SUCCESSFULLY_UPDATED, response.getStatus());
        assertEquals(MessageVarList.RSP_SUCCESS, response.getMessage());
        assertEquals(eventId, response.getContent());

        verify(eventMapper).updateEntityFromRequest(updateEvent, event);
        verify(eventRepository).save(event);
    }


    @Test
    void deleteEvent_success() {
        // Arrange
        String eventId = "abc123";
        String userName = "testuser";

        Event event = new Event();
        event.setEventId(eventId);
        event.setArchived(false); // initially not deleted

        when(eventRepository.findByEventId(eventId)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0)); // return saved entity

        // Act
        ResponseBean response = eventService.deleteEvent(eventId, userName);

        // Assert
        assertNotNull(response);
        assertEquals(MessageVarList.RSP_SUCCESS, response.getStatus());
        assertEquals(StatusVarList.SUCCESSFULLY_DELETED, response.getMessage());
        assertEquals(eventId, response.getContent());

        assertTrue(event.isArchived()); // ensure soft delete was set

        verify(eventRepository).findByEventId(eventId);
        verify(eventRepository).save(event);
        System.out.println("Response " + response);
    }


    @Test
    void filterEvents_success() {
        // Arrange
        EventFilterCriteria filter = new EventFilterCriteria();
        filter.setStartDate(LocalDateTime.of(2025, 5, 20, 10, 0));
        filter.setLocation("New York");
        filter.setVisibility(EventVisibilityType.PUBLIC);

        List<Event> events = List.of(new Event(), new Event());

        when(eventRepository.getList(filter.getStartDate(), filter.getLocation(), filter.getVisibility().name()))
                .thenReturn(events);

        // Act
        ResponseBean response = eventService.filterEvents(filter);

        // Assert
        assertNotNull(response);
        assertEquals(MessageVarList.RSP_SUCCESS, response.getStatus());
        assertEquals(StatusVarList.FETCHED_SUCCESS, response.getMessage());
        assertEquals(events, response.getContent());

        verify(eventRepository).getList(filter.getStartDate(), filter.getLocation(), filter.getVisibility().name());
        System.out.println("Response" + response);
    }


    @Test
    void testCheckEventId_whenEventIsUpcoming() throws ResourceNotFoundException {
        // Arrange
        String eventId = "event123";
        LocalDateTime now = LocalDateTime.now();
        Event event = new Event();
        event.setStartTime(now.plusDays(1));
        event.setEndTime(now.plusDays(2));

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // Act
        ResponseBean response = eventService.checkEventId(eventId);

        // Assert
        Assertions.assertEquals("UPCOMING", response.getContent());
        Assertions.assertEquals(MessageVarList.RSP_SUCCESS, response.getStatus());
        Assertions.assertEquals(StatusVarList.SUCCESS, response.getMessage());

        System.out.println("Response " + response);
    }

    @Test
    void testCheckEventId_whenEventIsOngoing() throws ResourceNotFoundException {
        String eventId = "event456";
        LocalDateTime now = LocalDateTime.now();
        Event event = new Event();
        event.setStartTime(now.minusHours(1));
        event.setEndTime(now.plusHours(1));

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        ResponseBean response = eventService.checkEventId(eventId);

        Assertions.assertEquals("ONGOING", response.getContent());
        System.out.println("Response " + response);
    }


    @Test
    void testCheckEventId_whenEventHasEnded() throws ResourceNotFoundException {
        String eventId = "event789";
        LocalDateTime now = LocalDateTime.now();
        Event event = new Event();
        event.setStartTime(now.minusDays(2));
        event.setEndTime(now.minusDays(1));

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        ResponseBean response = eventService.checkEventId(eventId);

        Assertions.assertEquals("ENDED", response.getContent());
        System.out.println("Response " + response);
    }


    @Test
    void testCheckEventId_whenEventNotFound_shouldThrowException() {
        String eventId = "nonExistentEvent";

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            eventService.checkEventId(eventId);
        });

    }


    @Test
    void testGetUserHostedAndAttendingEvents_success() throws NotFoundException {
        // Arrange
        String userId = "john123";
        User user = new User(); // You can set more fields if needed
        user.setUserName(userId);

        List<Event> event = new ArrayList<>();
        Event hostedEvent1 = new Event();
        hostedEvent1.setEventId("E1");
        hostedEvent1.setTitle("Spring Boot Workshop");
        hostedEvent1.setStartTime(LocalDateTime.of(2025, 5, 20, 10, 0));
        hostedEvent1.setEndTime(LocalDateTime.of(2025, 5, 20, 12, 0));
        event.add(hostedEvent1);


        List<Event> eventList= new ArrayList<>();
        Event hostedEvent2 = new Event();
        hostedEvent2.setEventId("E2");
        hostedEvent2.setTitle("Java Conference");
        hostedEvent2.setStartTime(LocalDateTime.of(2025, 6, 10, 9, 0));
        hostedEvent2.setEndTime(LocalDateTime.of(2025, 6, 10, 17, 0));
        eventList.add(hostedEvent2);

        List<EventDto> hostedDtos = List.of(new EventDto(), new EventDto());
        List<EventDto> attendingDtos = List.of(new EventDto());

        Mockito.when(userRepository.findByUserName(userId)).thenReturn(Optional.of(user));
        Mockito.when(eventRepository.findByhostId(user)).thenReturn(event);
        Mockito.when(attendanceRepository.findEventsByAndCompositeEvent_UserId(user)).thenReturn(eventList);


        EventService spyService = Mockito.spy(eventService);
        Mockito.doReturn(hostedDtos).when(eventService.convertEventsToDtos(event));
        Mockito.doReturn(attendingDtos).when(eventService.convertEventsToDtos(eventList));

        // Act
        Map<String, List<EventDto>> result = spyService.getUserHostedAndAttendingEvents(userId);

        // Assert
        Assertions.assertEquals(2, result.get("hosting").size());
        Assertions.assertEquals(1, result.get("attending").size());

        System.out.println(result);
    }


    @Test
    void testGetEventDetails_success() throws ResourceNotFoundException {
        // Arrange
        String eventId = "E100";
        Event event = new Event();
        event.setEventId(eventId);
        event.setTitle("Tech Meetup");
        event.setDescription("A meetup for tech enthusiasts.");
        event.setLocation("New York");
        event.setStartTime(LocalDateTime.of(2025, 6, 1, 10, 0));
        event.setEndTime(LocalDateTime.of(2025, 6, 1, 12, 0));
        event.setVisibility(EventVisibilityType.valueOf("PUBLIC"));

        long attendeeCount = 50L;

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        Mockito.when(attendanceRepository.countAttendeesByEventId(eventId)).thenReturn(attendeeCount);

        // Act
        ResponseBean response = eventService.getEventDetails(eventId);
        EventDetailsResponse details = (EventDetailsResponse) response.getContent();

        // Assert
        Assertions.assertEquals(MessageVarList.RSP_SUCCESS, response.getStatus());
        Assertions.assertEquals(StatusVarList.SUCCESS, response.getMessage());
        Assertions.assertEquals("Tech Meetup", details.getTitle());
        Assertions.assertEquals("New York", details.getLocation());
        Assertions.assertEquals(attendeeCount, details.getAttendeeCount());

        System.out.println(details);
    }

    @Test
    void testGetEventDetails_eventNotFound_shouldThrowException() {
        // Arrange
        String eventId = "UNKNOWN_ID";

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            eventService.getEventDetails(eventId);
        });
    }


}



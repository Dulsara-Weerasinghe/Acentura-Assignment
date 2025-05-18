package com.example.eventManagement.repository;

import com.example.eventManagement.entity.Attendance;
import com.example.eventManagement.entity.Event;
import com.example.eventManagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendenceRepository extends JpaRepository<Attendance, String> {
    List<Event> findEventsByAndCompositeEvent_UserId(User userId);


    @Query(value = "select a from Attendance a where a.compositeEvent.userId=:userId and a.compositeEvent.eventId.archived=false ")
    Optional<List<Attendance>> findByAttenceUsingUserId(@Param("userId") String userId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.compositeEvent.eventId = :eventId AND a.status = 'GOING'")
    long countAttendeesByEventId(@Param("eventId") String eventId);
}

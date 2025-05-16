package com.example.eventManagement.repository;

import com.example.eventManagement.entity.Attendance;
import com.example.eventManagement.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendenceRepository extends JpaRepository<Attendance,String> {
    List<Event> findEventsByUserId(String userId);


    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.eventId = :eventId AND a.status = 'GOING'")
    long countAttendeesByEventId(@Param("eventId") String eventId);
}

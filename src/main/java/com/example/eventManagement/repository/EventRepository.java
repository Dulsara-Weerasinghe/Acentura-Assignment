package com.example.eventManagement.repository;

import com.example.eventManagement.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends CrudRepository<Event, String> {


    @Query(value = "select  e from Event e where e.createdAt=:date and e.location=:location and e.visibility=:visible ")
    List<Event> getList(@Param("date")LocalDateTime date, @Param("location")String location,@Param("visible")String visible);

    @Query("SELECT e FROM Event e WHERE e.startTime >= :now")
   Page<Event> findUpcomingEvents(@Param("now") LocalDateTime now, Pageable pageable);

    List<Event> findByHostId(String userId);


}

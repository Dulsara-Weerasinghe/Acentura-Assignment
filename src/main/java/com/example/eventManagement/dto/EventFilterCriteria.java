package com.example.eventManagement.dto;

import com.example.eventManagement.enums.EventVisibilityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFilterCriteria {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private EventVisibilityType visibility; // enum: PUBLIC, PRIVATE

}

package com.example.eventManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterEvents {
    private String location;
    private String visibility;
    private LocalDateTime date;
}

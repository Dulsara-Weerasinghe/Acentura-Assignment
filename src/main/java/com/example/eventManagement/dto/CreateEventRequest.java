package com.example.eventManagement.dto;

import com.example.eventManagement.enums.EventVisibilityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {

  @NotBlank(message = "{CreateEventRequest.title.required}")
  @Size(min = 10,
      max = 100,
      message = "{CreateEventRequest.title.length.invalid}")
  private String title;
  @NotBlank(message = "{CreateEventRequest.description.required}")
  @Size(min = 10,
      max = 1000,
      message = "{CreateEventRequest.description.length.invalid}")
  private String description;
  @NotBlank(message = "{CreateEventRequest.hostId.required}")
  private String hostId;
  @NotBlank(message = "{CreateEventRequest.startTime.required}")
  private LocalDateTime startTime;
  @NotBlank(message = "{CreateEventRequest.endTime.required}")
  private LocalDateTime endTime;
  @NotBlank(message = "{CreateEventRequest.location.required}")
  private String location;

  private EventVisibilityType eventVisibilityType;
}

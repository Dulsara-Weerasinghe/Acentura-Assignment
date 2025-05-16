package com.example.eventManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "Attendance")
public class Attendance {


  @EmbeddedId
  private UserEventCompositeEvent compositeEvent;


  @Column(name = "STATUS", nullable = false)
  private String status;

  @Column(name = "RESPONDED_AT")
  private LocalDateTime respondedAt;
}

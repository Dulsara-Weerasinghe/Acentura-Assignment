package com.example.eventManagement.entity;

import com.example.eventManagement.enums.EventVisibilityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EVENTS")
@Builder
public class Event {

  @Id
  @Column(name = "EVENT_ID", nullable = false, unique = true)
  private String eventId;

  @Column(name = "TITLE", nullable = false, length = 255)
  private String title;

  @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
  private String description;

  // Soft delete flag
  @Column(name = "IS_ARCHIVED", nullable = false)
  private boolean archived = false;

//  An event is hosted by one user → Many-to-One with User
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "HOST_ID", referencedColumnName = "ID")
  private User hostId;

  @Column(name = "START_TIME", nullable = false)
  private LocalDateTime startTime;

  @Column(name = "END_TIME", nullable = false)
  private LocalDateTime endTime;

  @Column(name = "LOCATION", length = 255)
  private String location;

  @Enumerated(EnumType.STRING)
  @Column(name = "VISIBILITY", nullable = false, length = 20)
  private EventVisibilityType visibility;

  @Column(name = "CREATED_AT", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "UPDATED_AT")
  private LocalDateTime updatedAt;


    @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}

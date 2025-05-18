package com.example.eventManagement.entity;

import com.example.eventManagement.enums.UserRoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`user`")
public class User {

  @Id
  @Column(name = "ID", nullable = false, unique = true)
  private String id;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "email", nullable = false, unique = true, length = 150)
  private String email;


  @Column(name = "role")
  private String role;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "Password")
  private String password;

  @Column(name = "userName")
  private String userName;

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

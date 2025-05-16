package com.example.eventManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEvent {


    private String title;
    private String description;
    private String hostId;
    private String location;
    private String visibility;


}

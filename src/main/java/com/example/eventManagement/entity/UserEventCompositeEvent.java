package com.example.eventManagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@ToString
@Embeddable
public class UserEventCompositeEvent implements Serializable {


    @ManyToOne
    @JoinColumn(name = "EVENT_ID",referencedColumnName = "EVENT_ID")
    private Event eventId;


    @ManyToOne
    @JoinColumn(name = "USER_ID",referencedColumnName = "ID")
    private User userId;


}

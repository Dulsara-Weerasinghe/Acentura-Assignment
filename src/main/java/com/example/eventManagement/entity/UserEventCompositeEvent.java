package com.example.eventManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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

    @Column(name="EVENT_ID")
    private String eventId;

    @Column(name = "USER_ID")
    private String userId;


}

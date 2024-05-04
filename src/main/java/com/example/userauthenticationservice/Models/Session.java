package com.example.userauthenticationservice.Models;

import com.example.userauthenticationservice.enums.SessionStatus;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Session extends BaseModel {
    @ManyToOne
    User user;
    String token;
    Date createdAt;
    Date expiry;
    @Enumerated(EnumType.ORDINAL)
SessionStatus status;

}

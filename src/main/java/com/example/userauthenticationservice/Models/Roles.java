package com.example.userauthenticationservice.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Roles extends BaseModel{

    String value;
}

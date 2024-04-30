package com.example.userauthenticationservice.Dtos;

import com.example.userauthenticationservice.Models.Roles;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserRespDto {
    private String email;
    private Set<Roles> roles = new HashSet<>();
}

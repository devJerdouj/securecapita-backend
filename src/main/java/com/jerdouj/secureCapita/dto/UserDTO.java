package com.jerdouj.secureCapita.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String title;
    private String bio;
    private String imageUrl;
    private boolean enabled;
    private boolean nonLocked;
    private boolean usingMfa;
    private Instant createdAt;
}

package com.example.hotel_booking_portal.web.model.response;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private String role;
}

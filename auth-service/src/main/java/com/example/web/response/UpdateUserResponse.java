package com.example.web.response;

import lombok.Data;

@Data
public class UpdateUserResponse {

    private Long id;

    private String username;

    private String email;
}

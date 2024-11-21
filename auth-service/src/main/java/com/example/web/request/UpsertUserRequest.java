package com.example.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpsertUserRequest {

    @NotBlank(message = "Имя пользователя должно быть заполнено")
    @Size(min = 3, max = 30, message = "Имя пользователя не может быть меньше {min} и больше {max}")
    private String username;

    @NotBlank(message = "Пароль должен быть заполнен")
    private String password;

    @Email(message = "Email заполнен не верно: ${validatedValue}",
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;
}

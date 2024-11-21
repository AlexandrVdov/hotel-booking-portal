package com.example.hotel_booking_portal.web.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpsertBookingRequest {

    @NotNull(message = "Дата заезда не может быть пустой")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Дата выезда должна быть в формате yyyy-MM-dd")
    private String checkInDate;

    @NotNull(message = "Дата выезда не может быть пустой")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Дата выезда должна быть в формате yyyy-MM-dd")
    private String checkOutDate;

    @NotNull(message = "ID комнаты не может быть пустым")
    private Long roomId;

    @NotNull(message = "ID пользователя не может быть пустым")
    private Long userId;
}

package com.example.hotel_booking_portal.web.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UpsertRoomRequest {

    @NotBlank(message = "Имя комнаты должно быть заполнено")
    private String name;

    @NotBlank(message = "Описание комнаты должно быть заполнено")
    private String description;

    @NotNull(message = "Не указан номер комнаты")
    @Positive(message = "Номер комнаты должен быть больше 0")
    private Integer roomNumber;

    @NotNull(message = "Не указана цена комнаты")
    @Positive(message = "Цена комнаты должна быть больше 0")
    private Double price;

    @NotNull(message = "Не указан максимальное кол-во людей, которое можно разместить в комнате")
    @Positive(message = "Кол-во людей должно быть больше 0")
    private Integer maxOccupancy;

    private List<Date> unavailableDates;

    @NotNull(message = "Не указан id отеля в котором расположена комната")
    private Long hotelId;
}

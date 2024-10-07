package com.example.hotel_booking_portal.web.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpsertHotelRequest {

    @NotBlank(message = "Название отеля должно быть заполнено")
    private String name;

    @NotBlank(message = "Заголовок объявления отеля должен быть заполнен")
    private String titleAd;

    @NotBlank(message = "Город в котором расположен отель должен быть заполнен")
    private String city;

    @NotBlank(message = "Адрес отеля должно быть заполнено")
    private String address;

    @NotNull(message = "Расстояние от центра города должно быть заполнено")
    @Positive(message = "Расстояние должно быть больше 0")
    private Integer distanceFromCenter;
}

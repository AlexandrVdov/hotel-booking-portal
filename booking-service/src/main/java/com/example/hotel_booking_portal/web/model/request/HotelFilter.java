package com.example.hotel_booking_portal.web.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelFilter {

    public HotelFilter(Integer pageSize, Integer pageNumber) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    @NotNull(message = "Не указан параметр кол-во на страницу")
    private Integer pageSize;

    @NotNull(message = "Не указан параметр номер страницы")
    private Integer pageNumber;

    private Long id;

    private String name;

    private String announcementTitle;

    private String city;

    private String address;

    private Double distanceFromCityCenter;

    private Double rating;

    private Integer numberOfRating;
}

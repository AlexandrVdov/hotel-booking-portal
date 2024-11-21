package com.example.hotel_booking_portal.web.model.response;

import lombok.Data;

@Data
public class UpdateHotelResponse {

    private Long id;

    private String name;

    private String announcementTitle;

    private String city;

    private String address;

    private Double distanceFromCityCenter;
}

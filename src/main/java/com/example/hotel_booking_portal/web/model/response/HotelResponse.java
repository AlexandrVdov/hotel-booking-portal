package com.example.hotel_booking_portal.web.model.response;

import lombok.Data;

@Data
public class HotelResponse {

    private Long id;

    private String name;

    private String announcementTitle;

    private String city;

    private String address;

    private Double distanceFromCityCenter;

    private Integer rating;

    private Integer reviewCount;
}

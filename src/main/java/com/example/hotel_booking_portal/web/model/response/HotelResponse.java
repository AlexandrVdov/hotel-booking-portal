package com.example.hotel_booking_portal.web.model.response;

import lombok.Data;

@Data
public class HotelResponse {

    private Long id;

    private String name;

    private String titleAd;

    private String city;

    private String address;

    private Integer distanceFromCenter;

    private Integer rating;

    private Integer numberRatings;
}

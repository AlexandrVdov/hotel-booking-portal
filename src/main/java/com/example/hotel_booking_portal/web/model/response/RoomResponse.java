package com.example.hotel_booking_portal.web.model.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class RoomResponse {

    private Long id;

    private String name;

    private String description;

    private Integer roomNumber;

    private Double price;

    private Integer maxOccupancy;

    private List<Date> unavailableDates = new ArrayList<>();

    private HotelResponse hotel;

}

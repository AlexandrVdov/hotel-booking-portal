package com.example.hotel_booking_portal.web.model.response;

import lombok.Data;

@Data
public class UpdateRoomResponse {

    private Long id;

    private String name;

    private String description;

    private Integer roomNumber;

    private Double price;

    private Integer maxOccupancy;

}

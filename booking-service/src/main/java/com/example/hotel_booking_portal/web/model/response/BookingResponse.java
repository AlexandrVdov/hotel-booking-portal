package com.example.hotel_booking_portal.web.model.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingResponse {

    private Long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Long roomId;

    private Long userId;
}

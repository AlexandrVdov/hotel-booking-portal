package com.example.event;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingEvent {

    private Long userId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;
}

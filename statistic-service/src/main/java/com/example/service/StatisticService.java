package com.example.service;

import com.example.event.BookingEvent;
import com.example.event.RegistrationEvent;

public interface StatisticService {

    byte[] getStatisticAsCsv();

    void saveBookingEvent(BookingEvent event);

    void saveRegistrationEvent(RegistrationEvent event);
}

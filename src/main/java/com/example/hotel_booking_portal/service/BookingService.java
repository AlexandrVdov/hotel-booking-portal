package com.example.hotel_booking_portal.service;

import com.example.hotel_booking_portal.web.model.request.BookingFilter;
import com.example.hotel_booking_portal.web.model.request.UpsertBookingRequest;
import com.example.hotel_booking_portal.web.model.response.BookingListResponse;
import com.example.hotel_booking_portal.web.model.response.BookingResponse;

public interface BookingService {

    BookingListResponse findAll(BookingFilter filter);

    BookingResponse save(UpsertBookingRequest request);
}

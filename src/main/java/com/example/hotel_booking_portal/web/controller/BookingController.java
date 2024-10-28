package com.example.hotel_booking_portal.web.controller;

import com.example.hotel_booking_portal.service.BookingService;
import com.example.hotel_booking_portal.web.model.request.BookingFilter;
import com.example.hotel_booking_portal.web.model.request.UpsertBookingRequest;
import com.example.hotel_booking_portal.web.model.response.BookingListResponse;
import com.example.hotel_booking_portal.web.model.response.BookingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BookingListResponse> findAll(@Valid BookingFilter filter) {
        return ResponseEntity.ok(bookingService.findAll(filter));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody @Valid UpsertBookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.save(request));
    }
}

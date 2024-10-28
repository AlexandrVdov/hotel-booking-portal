package com.example.hotel_booking_portal.web.controller;

import com.example.hotel_booking_portal.service.HotelService;
import com.example.hotel_booking_portal.web.model.request.HotelFilter;
import com.example.hotel_booking_portal.web.model.request.UpsertHotelRequest;
import com.example.hotel_booking_portal.web.model.response.HotelListResponse;
import com.example.hotel_booking_portal.web.model.response.HotelResponse;
import com.example.hotel_booking_portal.web.model.response.UpdateHotelResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<HotelListResponse> findAll(@Valid HotelFilter filter) {
        return ResponseEntity.ok(hotelService.findAll(filter));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UpdateHotelResponse> createHotel(@RequestBody @Valid UpsertHotelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.save(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UpdateHotelResponse> updateHotel(@PathVariable Long id,
                                               @RequestBody @Valid UpsertHotelRequest request) {
        return ResponseEntity.ok(hotelService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}

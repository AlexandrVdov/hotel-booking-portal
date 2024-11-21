package com.example.hotel_booking_portal.web.controller;

import com.example.hotel_booking_portal.service.RoomService;
import com.example.hotel_booking_portal.web.model.request.RoomFilter;
import com.example.hotel_booking_portal.web.model.request.UpsertRoomRequest;
import com.example.hotel_booking_portal.web.model.response.RoomFilterListResponse;
import com.example.hotel_booking_portal.web.model.response.RoomResponse;
import com.example.hotel_booking_portal.web.model.response.UpdateRoomResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<RoomFilterListResponse> filterBy(@Valid RoomFilter filter) {
        return ResponseEntity.ok(roomService.filterBy(filter));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UpdateRoomResponse> createRoom(@RequestBody @Valid UpsertRoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.save(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UpdateRoomResponse> updateRoom(@PathVariable Long id,
                                                           @RequestBody @Valid UpsertRoomRequest request) {
        return ResponseEntity.ok(roomService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}

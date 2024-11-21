package com.example.hotel_booking_portal.service;

import com.example.hotel_booking_portal.web.model.request.RoomFilter;
import com.example.hotel_booking_portal.web.model.request.UpsertRoomRequest;
import com.example.hotel_booking_portal.web.model.response.RoomFilterListResponse;
import com.example.hotel_booking_portal.web.model.response.RoomResponse;
import com.example.hotel_booking_portal.web.model.response.UpdateRoomResponse;

public interface RoomService {

    RoomFilterListResponse filterBy(RoomFilter filter);

    RoomResponse findById(Long roomId);

    UpdateRoomResponse save(UpsertRoomRequest request);

    UpdateRoomResponse update(Long roomId, UpsertRoomRequest request);

    void deleteById(Long roomId);
}

package com.example.hotel_booking_portal.service;

import com.example.hotel_booking_portal.web.model.request.UpsertRoomRequest;
import com.example.hotel_booking_portal.web.model.response.RoomListResponse;
import com.example.hotel_booking_portal.web.model.response.RoomResponse;
import com.example.hotel_booking_portal.web.model.response.UpdateRoomResponse;

public interface RoomService {

    RoomResponse findById(Long roomId);

    UpdateRoomResponse save(UpsertRoomRequest request);

    UpdateRoomResponse update(Long roomId, UpsertRoomRequest request);

    void deleteById(Long roomId);
}

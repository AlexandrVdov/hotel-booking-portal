package com.example.hotel_booking_portal.web.model.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomListResponse {

    private List<RoomResponse> rooms = new ArrayList<>();
}

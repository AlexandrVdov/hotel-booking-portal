package com.example.hotel_booking_portal.web.model.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HotelListResponse {

    private List<HotelResponse> hotels = new ArrayList<>();
}

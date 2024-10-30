package com.example.hotel_booking_portal.web.model.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HotelFilterListResponse {

    private List<HotelResponse> hotels = new ArrayList<>();

    private Integer totalRecords;
}

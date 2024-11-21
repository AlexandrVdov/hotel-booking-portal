package com.example.hotel_booking_portal.service;

import com.example.hotel_booking_portal.web.model.request.HotelFilter;
import com.example.hotel_booking_portal.web.model.request.UpsertHotelRequest;
import com.example.hotel_booking_portal.web.model.response.HotelFilterListResponse;
import com.example.hotel_booking_portal.web.model.response.HotelListResponse;
import com.example.hotel_booking_portal.web.model.response.HotelResponse;
import com.example.hotel_booking_portal.web.model.response.UpdateHotelResponse;

public interface HotelService {

    HotelListResponse findAll(HotelFilter filter);

    HotelResponse findById(Long hotelId);

    UpdateHotelResponse save(UpsertHotelRequest request);

    UpdateHotelResponse update(Long hotelId, UpsertHotelRequest request);

    void deleteById(Long hotelId);

    HotelResponse updateRating(Long hotelId, Integer newMark);

    HotelFilterListResponse filterBy(HotelFilter filter);
}

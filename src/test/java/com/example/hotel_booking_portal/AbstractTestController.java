package com.example.hotel_booking_portal;

import com.example.hotel_booking_portal.entity.Hotel;
import com.example.hotel_booking_portal.web.controller.HotelController;
import com.example.hotel_booking_portal.web.model.request.UpsertHotelRequest;
import com.example.hotel_booking_portal.web.model.response.HotelResponse;
import com.example.hotel_booking_portal.web.model.response.UpdateHotelResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public abstract class AbstractTestController {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected Hotel createHotel(Long id, String name) {
        Hotel hotel = new Hotel();
        hotel.setId(id);
        hotel.setName(name);
        hotel.setTitleAd("Hotel Add");
        hotel.setCity("City");
        hotel.setAddress("Address");
        hotel.setRating(5);
        hotel.setNumberRatings(10);
        hotel.setDistanceFromCenter(100);

        return hotel;
    }

    protected HotelResponse createHotelResponse(Long id, String name) {
        HotelResponse hotelResponse = new HotelResponse();
        hotelResponse.setId(id);
        hotelResponse.setName(name);
        hotelResponse.setTitleAd("Hotel Add");
        hotelResponse.setCity("City");
        hotelResponse.setAddress("Address");
        hotelResponse.setRating(5);
        hotelResponse.setNumberRatings(10);
        hotelResponse.setDistanceFromCenter(100);

        return hotelResponse;
    }

    protected UpdateHotelResponse createUpdateHotelResponse(Long id, String name) {
        UpdateHotelResponse updateHotelResponse = new UpdateHotelResponse();
        updateHotelResponse.setId(id);
        updateHotelResponse.setName(name);
        updateHotelResponse.setTitleAd("Hotel Add");
        updateHotelResponse.setCity("City");
        updateHotelResponse.setAddress("Address");
        updateHotelResponse.setDistanceFromCenter(100);

        return updateHotelResponse;
    }

    protected UpsertHotelRequest createUpsertHotelRequest(Long id, String name) {
        UpsertHotelRequest upsertHotelRequest = new UpsertHotelRequest();
        upsertHotelRequest.setName(name);
        upsertHotelRequest.setTitleAd("Hotel Add");
        upsertHotelRequest.setCity("City");
        upsertHotelRequest.setAddress("Address");
        upsertHotelRequest.setDistanceFromCenter(100);

        return upsertHotelRequest;
    }
}

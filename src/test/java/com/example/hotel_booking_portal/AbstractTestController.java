package com.example.hotel_booking_portal;

import com.example.hotel_booking_portal.entity.Hotel;
import com.example.hotel_booking_portal.entity.Room;
import com.example.hotel_booking_portal.web.model.request.UpsertHotelRequest;
import com.example.hotel_booking_portal.web.model.request.UpsertRoomRequest;
import com.example.hotel_booking_portal.web.model.response.HotelResponse;
import com.example.hotel_booking_portal.web.model.response.RoomResponse;
import com.example.hotel_booking_portal.web.model.response.UpdateHotelResponse;
import com.example.hotel_booking_portal.web.model.response.UpdateRoomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public abstract class AbstractTestController {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected Hotel createHotel(Long id, String name) {
        Hotel hotel = new Hotel();
        hotel.setId(id);
        hotel.setName(name);
        hotel.setAnnouncementTitle("Hotel Add");
        hotel.setCity("City");
        hotel.setAddress("Address");
        hotel.setRating(5);
        hotel.setReviewCount(10);
        hotel.setDistanceFromCityCenter(100.);

        return hotel;
    }

    protected HotelResponse createHotelResponse(Long id, String name) {
        HotelResponse hotelResponse = new HotelResponse();
        hotelResponse.setId(id);
        hotelResponse.setName(name);
        hotelResponse.setAnnouncementTitle("Hotel Add");
        hotelResponse.setCity("City");
        hotelResponse.setAddress("Address");
        hotelResponse.setRating(5);
        hotelResponse.setReviewCount(10);
        hotelResponse.setDistanceFromCityCenter(100.);

        return hotelResponse;
    }

    protected UpdateHotelResponse createUpdateHotelResponse(Long id, String name) {
        UpdateHotelResponse updateHotelResponse = new UpdateHotelResponse();
        updateHotelResponse.setId(id);
        updateHotelResponse.setName(name);
        updateHotelResponse.setAnnouncementTitle("Hotel Add");
        updateHotelResponse.setCity("City");
        updateHotelResponse.setAddress("Address");
        updateHotelResponse.setDistanceFromCityCenter(100.);

        return updateHotelResponse;
    }

    protected UpsertHotelRequest createUpsertHotelRequest(Long id, String name) {
        UpsertHotelRequest upsertHotelRequest = new UpsertHotelRequest();
        upsertHotelRequest.setName(name);
        upsertHotelRequest.setAnnouncementTitle("Hotel Add");
        upsertHotelRequest.setCity("City");
        upsertHotelRequest.setAddress("Address");
        upsertHotelRequest.setDistanceFromCityCenter(100.);

        return upsertHotelRequest;
    }

    protected Room createRoom(Long id, String name) {
        Room room = new Room();
        room.setId(id);
        room.setName(name);
        room.setDescription("Room text");
        room.setRoomNumber(12);
        room.setPrice(120.3);
        room.setMaxOccupancy(3);

        return room;
    }

    protected RoomResponse createRoomResponse(Long id, String name) {
        HotelResponse hotel = createHotelResponse(1L, "Hotel");

        RoomResponse roomResponse = new RoomResponse();
        roomResponse.setId(id);
        roomResponse.setName(name);
        roomResponse.setDescription("Room text");
        roomResponse.setRoomNumber(12);
        roomResponse.setPrice(120.3);
        roomResponse.setMaxOccupancy(3);
        roomResponse.setHotel(hotel);

        return roomResponse;
    }

    protected UpdateRoomResponse createUpdateRoomResponse(Long id, String name) {
        UpdateRoomResponse updateRoomResponse = new UpdateRoomResponse();
        updateRoomResponse.setId(id);
        updateRoomResponse.setName(name);
        updateRoomResponse.setDescription("Room text");
        updateRoomResponse.setRoomNumber(12);
        updateRoomResponse.setPrice(120.3);
        updateRoomResponse.setMaxOccupancy(3);

        return updateRoomResponse;
    }

    protected UpsertRoomRequest createUpsertRoomRequest(Long id, String name) {
        UpsertRoomRequest upsertRoomRequest = new UpsertRoomRequest();
        upsertRoomRequest.setName(name);
        upsertRoomRequest.setDescription("Room text");
        upsertRoomRequest.setRoomNumber(12);
        upsertRoomRequest.setPrice(120.3);
        upsertRoomRequest.setMaxOccupancy(3);
        upsertRoomRequest.setHotelId(1L);

        return upsertRoomRequest;
    }
}

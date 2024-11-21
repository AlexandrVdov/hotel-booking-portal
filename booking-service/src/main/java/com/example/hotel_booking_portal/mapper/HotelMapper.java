package com.example.hotel_booking_portal.mapper;

import com.example.domain.Hotel;
import com.example.hotel_booking_portal.web.model.request.UpsertHotelRequest;
import com.example.hotel_booking_portal.web.model.response.HotelFilterListResponse;
import com.example.hotel_booking_portal.web.model.response.HotelListResponse;
import com.example.hotel_booking_portal.web.model.response.HotelResponse;
import com.example.hotel_booking_portal.web.model.response.UpdateHotelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {RoomMapper.class})
public interface HotelMapper {

    Hotel requestToHotel(UpsertHotelRequest request);

    @Mapping(source = "hotelId", target = "id")
    Hotel requestToHotel(Long hotelId, UpsertHotelRequest request);

    HotelResponse hotelToResponse(Hotel hotel);

    UpdateHotelResponse hotelToUpdateResponse(Hotel hotel);

    default HotelListResponse hotelListToHotelListResponse(List<Hotel> hotels) {
        HotelListResponse response = new HotelListResponse();

        response.setHotels(hotels.stream()
                .map(this::hotelToResponse)
                .collect(Collectors.toList()));

        return response;
    }

    default HotelFilterListResponse hotelListToHotelFilterListResponse(List<Hotel> hotels) {
        HotelFilterListResponse response = new HotelFilterListResponse();

        response.setHotels(hotels.stream()
                .map(this::hotelToResponse)
                .collect(Collectors.toList()));
        response.setTotalRecords(hotels.size());

        return response;
    }
}
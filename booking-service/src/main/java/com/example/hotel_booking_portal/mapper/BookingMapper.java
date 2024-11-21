package com.example.hotel_booking_portal.mapper;

import com.example.domain.Booking;
import com.example.event.BookingEvent;
import com.example.hotel_booking_portal.web.model.request.UpsertBookingRequest;
import com.example.hotel_booking_portal.web.model.response.*;
import com.example.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {RoomMapper.class, UserMapper.class})
public interface BookingMapper {

    Booking requestToBooking(UpsertBookingRequest request);

    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "user.id", target = "userId")
    BookingResponse bookingToResponse(Booking booking);

    @Mapping(source = "user.id", target = "userId")
    BookingEvent bookingToBookingEvent(Booking booking);

    default BookingListResponse bookingListToBookingListResponse(List<Booking> bookings) {
        BookingListResponse response = new BookingListResponse();

        response.setBookings(bookings.stream()
                .map(this::bookingToResponse)
                .collect(Collectors.toList()));

        return response;
    }
}

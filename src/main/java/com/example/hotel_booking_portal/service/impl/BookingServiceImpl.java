package com.example.hotel_booking_portal.service.impl;

import com.example.hotel_booking_portal.entity.Booking;
import com.example.hotel_booking_portal.entity.Room;
import com.example.hotel_booking_portal.entity.User;
import com.example.hotel_booking_portal.exception.EntityNotAvailableException;
import com.example.hotel_booking_portal.exception.EntityNotFoundException;
import com.example.hotel_booking_portal.mapper.BookingMapper;
import com.example.hotel_booking_portal.repository.BookingRepository;
import com.example.hotel_booking_portal.repository.RoomRepository;
import com.example.hotel_booking_portal.repository.UserRepository;
import com.example.hotel_booking_portal.service.BookingService;
import com.example.hotel_booking_portal.web.model.request.BookingFilter;
import com.example.hotel_booking_portal.web.model.request.UpsertBookingRequest;
import com.example.hotel_booking_portal.web.model.response.BookingListResponse;
import com.example.hotel_booking_portal.web.model.response.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final RoomRepository roomRepository;

    private final UserRepository userRepository;

    @Override
    public BookingListResponse findAll(BookingFilter filter) {
        return bookingMapper.bookingListToBookingListResponse(
                bookingRepository.findAll(
                        PageRequest.of(filter.getPageNumber(), filter.getPageSize())
                ).getContent()
        );
    }

    @Override
    public BookingResponse save(UpsertBookingRequest request) {

        Booking newBooking = bookingMapper.requestToBooking(request);

        Room existedRoom = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Комната не найдена с id {0}", request.getRoomId())
                ));
        User existedUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormat.format("Пользователь не найден с id {0}", request.getUserId())
                ));

        LocalDate checkInDate = LocalDate.parse(request.getCheckInDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate checkOutDate = LocalDate.parse(request.getCheckOutDate(), DateTimeFormatter.ISO_LOCAL_DATE);

        if (!isRoomAvailable(existedRoom, checkInDate, checkOutDate)) {
            throw new EntityNotAvailableException("Комната недоступна на указанные даты");
        }

        newBooking.setRoom(existedRoom);
        newBooking.setUser(existedUser);
        List<LocalDate> unavailableDates = addDates(existedRoom.getUnavailableDates(), checkInDate, checkOutDate);
        existedRoom.setUnavailableDates(unavailableDates);
        roomRepository.save(existedRoom);

        return bookingMapper.bookingToResponse(bookingRepository.save(newBooking));
    }

    private boolean isRoomAvailable(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        List<LocalDate> unavailableDates = room.getUnavailableDates();
        for (LocalDate date : unavailableDates) {
            if (!date.isBefore(checkInDate) && !date.isAfter(checkOutDate)) {
                return false;
            }
        }
        return true;
    }

    private List<LocalDate> addDates(List<LocalDate> dates, LocalDate checkInDate, LocalDate checkOutDate) {
        while (!checkInDate.isAfter(checkOutDate)) {
            dates.add(checkInDate);
            checkInDate = checkInDate.plusDays(1);
        }
        return dates;
    }
}

package com.example.service;

import com.example.entity.BookingData;
import com.example.entity.Registration;
import com.example.entity.Statistic;
import com.example.event.BookingEvent;
import com.example.event.RegistrationEvent;
import com.example.mapper.BookingDataMapper;
import com.example.mapper.RegistrationMapper;
import com.example.repository.BookingDataRepository;
import com.example.repository.RegistrationRepository;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final BookingDataRepository bookingDataRepository;

    private final RegistrationRepository registrationRepository;

    private final BookingDataMapper bookingDataMapper;

    private final RegistrationMapper userMapper;

    public byte[] getStatisticAsCsv() {
        List<Statistic> statisticList = new ArrayList<>();

        List<BookingData> bookingDataList = bookingDataRepository.findAll();
        for (BookingData booking : bookingDataList) {
            statisticList.add(new Statistic(
                    "booking",
                    booking.getUserId(),
                    booking.getCheckInDate().toString(),
                    booking.getCheckOutDate().toString()
            ));
        }

        List<Registration> registrationList = registrationRepository.findAll();
        for (Registration registration : registrationList) {
            statisticList.add(new Statistic(
                    "registration",
                    registration.getUserId(),
                    null,
                    null
            ));
        }

        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.builder()
                .addColumn("eventType")
                .addColumn("userId")
                .addColumn("checkInDate")
                .addColumn("checkOutDate")
                .setUseHeader(true)
                .build();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            csvMapper.writer(schema).writeValue(outputStream, statisticList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return outputStream.toByteArray();
    }

    @Override
    public void saveBookingEvent(BookingEvent event) {
        BookingData newBooking = bookingDataMapper.eventToBookingData(event);
        newBooking.setId(UUID.randomUUID().toString());

        bookingDataRepository.save(newBooking);
    }

    @Override
    public void saveRegistrationEvent(RegistrationEvent event) {
        Registration newRegistration = userMapper.eventToRegistrationData(event);
        newRegistration.setId(UUID.randomUUID().toString());

        registrationRepository.save(newRegistration);
    }
}

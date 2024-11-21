package com.example.hotel_booking_portal.service;

import com.example.domain.Booking;
import com.example.event.BookingEvent;
import com.example.hotel_booking_portal.mapper.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageService {

    @Value("${app.kafka.kafkaStatisticBookingTopic}")
    private String kafkaStatisticBookingTopic;

    private final KafkaTemplate<String, BookingEvent> bookingEventKafkaTemplate;

    private final BookingMapper bookingMapper;

    public void sendBookingMessage(Booking booking) {
        bookingEventKafkaTemplate.send(kafkaStatisticBookingTopic, bookingMapper.bookingToBookingEvent(booking));
    }
}

package com.example.listener;

import com.example.event.BookingEvent;
import com.example.event.RegistrationEvent;
import com.example.service.StatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageListener {

    private final StatisticService statisticService;

    private static String lastMessage;

    @KafkaListener(topics = "${app.kafka.kafkaStatisticBookingTopic}",
            groupId = "${app.kafka.kafkaStatisticGroupId}",
            containerFactory = "bookingEventConcurrentKafkaListenerContainerFactory")
    public void listenBooking(@Payload BookingEvent message) {
        log.info("Received message: {}", message);

        lastMessage = message.toString();

        statisticService.saveBookingEvent(message);
    }

    public String getLastMessage() {
        return lastMessage;
    }

    @KafkaListener(topics = "${app.kafka.kafkaStatisticRegistrationTopic}",
            groupId = "${app.kafka.kafkaStatisticGroupId}",
            containerFactory = "registrationEventConcurrentKafkaListenerContainerFactory")
    public void listenUserRegistration(@Payload RegistrationEvent message) {
        log.info("Received message: {}", message);

        lastMessage = message.toString();

        statisticService.saveRegistrationEvent(message);
    }
}

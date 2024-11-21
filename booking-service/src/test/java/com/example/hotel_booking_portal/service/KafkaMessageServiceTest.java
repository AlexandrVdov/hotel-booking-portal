package com.example.hotel_booking_portal.service;

import com.example.domain.Booking;
import com.example.domain.User;
import com.example.event.BookingEvent;
import com.example.hotel_booking_portal.AbstractTestController;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@EmbeddedKafka(partitions = 1,
        brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" },
        topics = {"statistic-booking-topic"})
public class KafkaMessageServiceTest extends AbstractTestController {

    @Autowired
    private KafkaMessageService kafkaMessageService;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    public void testSendBookingMessage() {
        User user = new User();
        user.setId(1L);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCheckInDate(LocalDate.of(2020, 3, 3));
        booking.setCheckOutDate(LocalDate.of(2020, 3, 4));

        kafkaMessageService.sendBookingMessage(booking);

        Map<String, Object> consumerProps = KafkaTestUtils
                .consumerProps("testGroup", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        consumerProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        KafkaConsumer<String, BookingEvent> consumer = new KafkaConsumer<>(consumerProps);

        consumer.subscribe(Collections.singletonList("statistic-booking-topic"));

        ConsumerRecord<String, BookingEvent> record = KafkaTestUtils
                .getSingleRecord(consumer, "statistic-booking-topic");

        assertThat(record).isNotNull();
        assertThat(record.value().getUserId()).isEqualTo(booking.getUser().getId());
    }
}

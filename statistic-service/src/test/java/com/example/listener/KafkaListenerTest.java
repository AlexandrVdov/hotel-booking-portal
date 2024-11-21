package com.example.listener;

import com.example.AbstractTestController;
import com.example.event.BookingEvent;
import com.example.service.StatisticService;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KafkaListenerTest extends AbstractTestController {

    private static String bootstrapServers = "";

    @Container
    public static KafkaContainer kafkaContainer =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    @BeforeAll
    public static void setUp() {
        kafkaContainer.start();

        bootstrapServers = kafkaContainer.getBootstrapServers().replace("PLAINTEXT://", "");

        createTopic("statistic-booking-topic", 1, 1);
    }

    private static void createTopic(String topicName, int partitions, int replicationFactor) {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
        try (AdminClient adminClient = AdminClient.create(props)) {
            NewTopic newTopic = new NewTopic(topicName, partitions, (short) replicationFactor);
            adminClient.createTopics(Collections.singleton(newTopic));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> bootstrapServers);
    }

    @MockBean
    private StatisticService statisticService;

    @Autowired
    private KafkaMessageListener kafkaMessageListener;

    @Test
    public void testListenBooking() throws Exception {
        BookingEvent bookingEvent = new BookingEvent();
        bookingEvent.setUserId(1L);

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        KafkaTemplate<String, BookingEvent> kafkaTemplate = new KafkaTemplate<>(
                new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(), new JsonSerializer<>(objectMapper)));

        kafkaTemplate.send("statistic-booking-topic", bookingEvent);

        await().pollInterval(Duration.ofSeconds(1))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    assertEquals(bookingEvent.toString(), kafkaMessageListener.getLastMessage());
                    verify(statisticService).saveBookingEvent(any(BookingEvent.class));
                });
    }
}

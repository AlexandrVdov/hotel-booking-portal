package com.example.service;

import com.example.domain.User;
import com.example.event.RegistrationEvent;
import com.example.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaAuthMessageService {

    @Value("${app.kafka.kafkaStatisticRegistrationTopic}")
    private String kafkaStatisticRegistrationTopic;

    private final KafkaTemplate<String, RegistrationEvent> registrationEventKafkaTemplate;

    private final UserMapper userMapper;

    public void sendRegistrationMessage(User user) {
        registrationEventKafkaTemplate.send(kafkaStatisticRegistrationTopic, userMapper.userToRegistrationEvent(user));
    }
}

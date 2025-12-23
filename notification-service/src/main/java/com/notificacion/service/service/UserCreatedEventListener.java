package com.notificacion.service.service;

import com.example.model.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserCreatedEventListener {

    @KafkaListener(
            topics = "${app.kafka.user-creation-topic}",
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )

    public void handleUserCreated(UserCreatedEvent event) {
        log.info("New event created received: {}", event);
    }
}

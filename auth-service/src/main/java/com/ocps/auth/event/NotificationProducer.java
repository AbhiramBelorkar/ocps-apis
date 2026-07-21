package com.ocps.auth.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    @Autowired
    private KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public void send(NotificationEvent event) {
        kafkaTemplate.send("notification-topic", event);
    }
}

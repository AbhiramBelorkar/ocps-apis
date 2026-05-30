package com.ocps.notification.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @KafkaListener(
            topics = "notification-topic",
            groupId = "notification-group"
    )
    public void consume(NotificationEvent event) {
        System.out.println("Recipient: " + event.getRecipient());
        System.out.println("Message: " + event.getMessage());
        System.out.println("Type: " + event.getType());
    }
}

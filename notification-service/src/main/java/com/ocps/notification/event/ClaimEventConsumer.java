package com.ocps.notification.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ClaimEventConsumer {

    @KafkaListener(
            topics = "claim-topic",
            groupId = "notification-group")
    public void consume(ClaimSubmittedEvent event) {
        System.out.println("Claim Received");
        System.out.println(event.getTrackingId());
        System.out.println(event.getUan());
        System.out.println(event.getStatus());
    }
}
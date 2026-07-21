package com.ocps.claim.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ClaimEventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void publishClaimSubmitted(ClaimSubmittedEvent event) {
        kafkaTemplate.send("claim-topic", event);
    }
}
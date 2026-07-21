package com.ocps.events.producer;

import com.ocps.events.event.ClaimSubmittedEvent;
import com.ocps.events.topic.KafkaTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ClaimSubmitProducer {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    public void publish(ClaimSubmittedEvent event){
        kafkaTemplate.send(KafkaTopic.CLAIM_TOPIC, event);
    }

}

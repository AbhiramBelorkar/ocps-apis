package com.ocps.events.producer;

import com.ocps.events.event.NotificationEvent;
import com.ocps.events.topic.KafkaTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    public void send(NotificationEvent event){
        kafkaTemplate.send(KafkaTopic.NOTIFICATION_TOPIC, event);
    }

}

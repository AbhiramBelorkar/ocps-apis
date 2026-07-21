package com.ocps.events.kafka;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.ocps.events.event.ClaimSubmittedEvent;
import com.ocps.events.event.NotificationEvent;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

public class KafkaConsumerConfig {

    @Configuration
    public class KafkaConsumerConfig {

        @Bean
        public ConsumerFactory<String, NotificationEvent> notificationConsumerFactory(KafkaProperties properties){
            Map<String,Object> configs = properties.buildConsumerProperties();

            JsonDeserializer<NotificationEvent> deserializer = new JsonDeserializer<>(NotificationEvent.class);

            deserializer.addTrustedPackages("*");
            deserializer.setUseTypeHeaders(false);

            return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), deserializer);
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, NotificationEvent> notificationKafkaListenerFactory(
                ConsumerFactory<String, NotificationEvent> consumerFactory){
            ConcurrentKafkaListenerContainerFactory<String, NotificationEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory);
            return factory;
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, ClaimSubmittedEvent> claimKafkaListenerFactory(
                ConsumerFactory<String, ClaimSubmittedEvent> consumerFactory){
            ConcurrentKafkaListenerContainerFactory<String, ClaimSubmittedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory);
            return factory;
        }

    }
}

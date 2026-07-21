package com.ocps.events.kafka;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

public class KafkaProducerConfig {

    @Configuration
    public class KafkaProducerConfig {

        @Bean
        public ProducerFactory<String,Object> producerFactory(KafkaProperties properties){
            Map<String,Object> configs = properties.buildProducerProperties();
            configs.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
            return new DefaultKafkaProducerFactory<>(configs);
        }

        @Bean
        public KafkaTemplate<String,Object> kafkaTemplate(ProducerFactory<String,Object> producerFactory){
            return new KafkaTemplate<>(producerFactory);
        }

    }
}

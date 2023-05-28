package com.example.SnmpReciever.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.snmp4j.PDU;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // Set additional Kafka producer properties

        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(), new StringSerializer());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
//@Bean
//public ConsumerFactory<String, PDU> consumerFactory() {
//    Map<String, Object> props = new HashMap<>();
//    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//    props.put(ConsumerConfig.GROUP_ID_CONFIG, "TRAPId");
//    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
//            new JsonDeserializer<>(PDU.class));
//}
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, PDU> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, PDU>
//                factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
}


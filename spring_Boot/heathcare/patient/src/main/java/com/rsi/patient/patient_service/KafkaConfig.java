package com.rsi.patient.patient_service;


import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    // ==================== TOPIC NAMES ====================

    public static final String TOPIC_PATIENT_REGISTERED = "patient.registered";
    public static final String TOPIC_PATIENT_UPDATED = "patient.updated";
    public static final String TOPIC_PATIENT_DISCHARGED = "patient.discharged";
    public static final String TOPIC_MEDICAL_HISTORY_UPDATED = "medical.history.updated";
    public static final String TOPIC_EMERGENCY_TRIGGERED = "emergency.triggered";
    public static final String TOPIC_DOCTOR_ASSIGNED = "patient.doctor.assigned";
    public static final String TOPIC_NURSE_ASSIGNED = "patient.nurse.assigned";
    public static final String TOPIC_HIGH_RISK_DETECTED = "patient.highrisk.detected";

    // ==================== PRODUCER CONFIGURATION ====================

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.RETRIES_CONFIG, 3);
        config.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, true);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // ==================== CONSUMER CONFIGURATION ====================

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "patient-service-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        config.put(JsonDeserializer.TYPE_MAPPINGS,
                "doctorAssignedEvent:com.rsi.patient.patient_service.events.consumed.DoctorAssignedEvent," +
                        "nurseAssignedEvent:com.rsi.patient.patient_service.events.consumed.NurseAssignedEvent");

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    // ==================== TOPIC CREATION ====================

    @Bean
    public NewTopic patientRegisteredTopic() {
        return TopicBuilder.name(TOPIC_PATIENT_REGISTERED)
                .partitions(3)
                .replicas(1)
                .config("retention.ms", "604800000") // 7 days
                .build();
    }

    @Bean
    public NewTopic patientUpdatedTopic() {
        return TopicBuilder.name(TOPIC_PATIENT_UPDATED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic patientDischargedTopic() {
        return TopicBuilder.name(TOPIC_PATIENT_DISCHARGED)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic medicalHistoryUpdatedTopic() {
        return TopicBuilder.name(TOPIC_MEDICAL_HISTORY_UPDATED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic emergencyTriggeredTopic() {
        return TopicBuilder.name(TOPIC_EMERGENCY_TRIGGERED)
                .partitions(5)
                .replicas(1)
                .config("retention.ms", "259200000") // 3 days
                .build();
    }

    @Bean
    public NewTopic doctorAssignedTopic() {
        return TopicBuilder.name(TOPIC_DOCTOR_ASSIGNED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic nurseAssignedTopic() {
        return TopicBuilder.name(TOPIC_NURSE_ASSIGNED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic highRiskDetectedTopic() {
        return TopicBuilder.name(TOPIC_HIGH_RISK_DETECTED)
                .partitions(2)
                .replicas(1)
                .build();
    }
}
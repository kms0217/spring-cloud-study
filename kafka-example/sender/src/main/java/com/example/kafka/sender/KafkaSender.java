package com.example.kafka.sender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaSender {

    private static final String TOPIC_NAME = "testTopic";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String msg) {
        try{
            kafkaTemplate.send(TOPIC_NAME, msg);
        } catch (Exception e) {
            log.error("error" + e.getMessage());
        }
    }
}

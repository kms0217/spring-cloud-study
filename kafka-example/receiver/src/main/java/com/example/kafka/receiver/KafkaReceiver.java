package com.example.kafka.receiver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaReceiver {

    private static final String TOPIC_NAME = "testTopic";
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = TOPIC_NAME, autoStartup = "true")
    public void receive(String msg) {
        log.info("msg : " + msg);
    }
}

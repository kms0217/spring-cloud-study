package com.example.rabbitmq.sender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitSender {

    private final RabbitTemplate rabbitTemplate;

    public void publish(String exchange, String msg) {
        try {
            rabbitTemplate.convertAndSend(exchange, "testRoute", msg);
        } catch (Exception e) {
            log.error("error", e);
        }
    }
}

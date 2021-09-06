package com.example.rabbitmq.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitReceiver {

    @RabbitListener(queues = "testQueue")
    public void receiveMessage(Message msg) {
        log.info(msg.toString());
    }
}

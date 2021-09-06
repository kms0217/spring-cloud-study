package com.example.rabbitmq.sender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final RabbitSender rabbitSender;

    @GetMapping("/send")
    public String sendData(@RequestParam(defaultValue = "") String msg) {
        rabbitSender.publish("test", msg);
        return "send Data";
    }
}

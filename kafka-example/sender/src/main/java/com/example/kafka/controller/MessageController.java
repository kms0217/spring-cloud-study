package com.example.kafka.controller;

import com.example.kafka.sender.KafkaSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final KafkaSender kafkaSender;

    @GetMapping("/send")
    public String sendData(@RequestParam(defaultValue = "") String msg) {
        kafkaSender.send(msg);
        return "send Data";
    }
}

package com.example.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    private static final String EXCHANGE_NAME = "test";
    private static final String QUEUE_NAME = "testQueue";
    private static final String ROUTING_KEY = "testRoute";

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Queue queue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with(ROUTING_KEY);
    }
}

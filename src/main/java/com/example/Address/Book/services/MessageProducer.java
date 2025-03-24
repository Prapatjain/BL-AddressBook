package com.example.Address.Book.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.Address.Book.configuration.RabbitMQConfig.EXCHANGE_NAME;
import static com.example.Address.Book.configuration.RabbitMQConfig.ROUTING_KEY;


@Service
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
        System.out.println("Sent message to RabbitMQ: " + message);
    }
}
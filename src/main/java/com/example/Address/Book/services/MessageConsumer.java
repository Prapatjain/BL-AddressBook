package com.example.Address.Book.services;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.Address.Book.configuration.RabbitMQConfig.QUEUE_NAME;

@Component
public class MessageConsumer {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = QUEUE_NAME)
    public void receiveMessage(String message) {
        System.out.println("Received message from RabbitMQ: " + message);

        // Split msg to identify (REGISTER or FORGOT or RESET)
        String[] data = message.split("\\|");
        String messageType = data[0];
        String email = data[1];
        String firstName = data[2];

        if (messageType.equals("REGISTER")) {

            String body = "Hi " + firstName + ",\n" + "You have been successfully registered";

            emailService.sendEmail(email, "Registration Successful", body);

        }
        else if(messageType.equals("FORGOT")){

            String body = "Hi " + firstName + ",\n" + "Your password has been changed";

            emailService.sendEmail(email, "Forgot Password", body);

        }
        else if(messageType.equals("RESET")){

            String body = "Hi " + firstName + ",\n" + "Your password has been reset";

            emailService.sendEmail(email, "Reset Password", body);

        }
    }
}
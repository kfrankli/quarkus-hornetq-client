package com.example.kfrankli;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;

@ApplicationScoped
public class MessageConsumer implements Runnable {

    @Inject
    ConnectionFactory connectionFactory;

    void onStart(@Observes StartupEvent ev) {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try (JMSContext context = connectionFactory.createContext()) {
            Queue queue = context.createQueue("ExampleQueue");
            var consumer = context.createConsumer(queue);
            while (true) {
                Message m = consumer.receive();
                if (m instanceof TextMessage) {
                    System.out.println("<<< Received: " + ((TextMessage) m).getText());
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
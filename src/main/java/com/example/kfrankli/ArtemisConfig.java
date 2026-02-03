package com.example.kfrankli;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

@ApplicationScoped
public class ArtemisConfig {

    @Produces
    @ApplicationScoped
    public ConnectionFactory connectionFactory() {
        try {
            ActiveMQConnectionFactory factory = ActiveMQJMSClient.createConnectionFactory(
                "tcp://localhost:5445", "quarkus-client"
            );

            factory.setProtocolManagerFactoryStr(
                "org.apache.activemq.artemis.core.protocol.hornetq.client.HornetQClientProtocolManagerFactory"
            );

            // ADD THESE TWO LINES
            factory.setUser("quarkus");
            factory.setPassword("Password123!");

            factory.setReconnectAttempts(0);
            return factory;
        } catch (Exception e) {
            throw new RuntimeException("Failed to bridge to HornetQ", e);
        }
    }
}
package com.example.kfrankli;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/jms")
@ApplicationScoped
public class MessagingResource {

    @Inject
    ConnectionFactory connectionFactory;

    @GET
    @Path("/send")
    @Produces(MediaType.TEXT_PLAIN)
    public String sendMessage(@QueryParam("msg") String msg) {
        String messageBody = (msg != null) ? msg : "Default trivial message";
        
        try (JMSContext context = connectionFactory.createContext()) {
            // "ExampleQueue" must match the name in JBoss EAP
            Queue queue = context.createQueue("ExampleQueue");
            context.createProducer().send(queue, messageBody);
            return "Successfully sent: " + messageBody;
        } catch (Exception e) {
            return "Error sending message: " + e.getMessage();
        }
    }
}
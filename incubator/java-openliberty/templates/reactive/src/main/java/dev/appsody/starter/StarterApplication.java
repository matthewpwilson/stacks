package dev.appsody.starter;

import java.util.logging.Logger;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StarterApplication {
    private static Logger logger = Logger.getLogger(StarterApplication.class.getName());

    @Incoming("incomingTopic1")
    @Outgoing("outgoingTopic1")
    public String processMessage(String message) {
        logger.info("Message " + message);

        return message;
    }

    @Incoming("incomingTopic2")
    public void receiveMessage(String message) {
        logger.info("Message " + message);
    }
}

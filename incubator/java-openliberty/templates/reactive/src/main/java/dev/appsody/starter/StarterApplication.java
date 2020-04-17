package dev.appsody.starter;


import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import javax.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@ApplicationScoped
public class StarterApplication {
    private static Logger logger = LoggerFactory.getLogger(StarterApplication.class);

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

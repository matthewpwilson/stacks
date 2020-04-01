package dev.appsody.starter;

import java.util.logging.Logger;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationPath("/starter")
public class StarterApplication extends Application {
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

package topic.solace;

import com.solace.messaging.MessagingService;
import com.solace.messaging.PubSubPlusClientException;
import com.solace.messaging.config.SolaceProperties;
import com.solace.messaging.config.profile.ConfigurationProfile;
import com.solace.messaging.publisher.OutboundMessage;
import com.solace.messaging.publisher.OutboundMessageBuilder;
import com.solace.messaging.publisher.PersistentMessagePublisher;
import com.solace.messaging.resources.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SolaceSender
 * @Author yyl
 * @Date 2024-02-05 15:52:20
 * @Description SolaceSender
 * @Version 1.0
 */
public class SolaceSender {

    private static final String SAMPLE_NAME = SolaceSender.class.getSimpleName();
    private static final Logger logger = LogManager.getLogger();
    private static final String TOPIC_PREFIX = "huawei/sales/channel/orders/v1/created";  // used as the topic "root"
    private static final String API = "Java";
    private static final int APPROX_MSG_RATE_PER_SEC = 100;
    private static final int PAYLOAD_SIZE = 512;
    private static volatile int msgSentCounter = 0;                   // num messages sent
    private static volatile boolean isShutdown = false;

    public static void main(String[] args) throws IOException {
        final Properties properties = new Properties();
        properties.setProperty(SolaceProperties.TransportLayerProperties.HOST, "tcp://192.168.1.162:55556");          // host:port
        properties.setProperty(SolaceProperties.ServiceProperties.VPN_NAME,  "default");     // message-vpn
        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_USER_NAME, "default");      // client-username
        properties.setProperty(SolaceProperties.AuthenticationProperties.SCHEME_BASIC_PASSWORD, "default");  // client-password
        properties.setProperty(SolaceProperties.TransportLayerProperties.RECONNECTION_ATTEMPTS, "20");  // recommended settings
        properties.setProperty(SolaceProperties.TransportLayerProperties.CONNECTION_RETRIES_PER_HOST, "5");
        // https://docs.solace.com/Solace-PubSub-Messaging-APIs/API-Developer-Guide/Configuring-Connection-T.htm
        // ready to connect now
        final MessagingService messagingService = MessagingService.builder(ConfigurationProfile.V1)
                .fromProperties(properties)
                .build();
        messagingService.connect();  // blocking connect
        messagingService.addServiceInterruptionListener(serviceEvent -> {
            logger.warn("### SERVICE INTERRUPTION: "+serviceEvent.getCause());
            //isShutdown = true;
        });
        messagingService.addReconnectionAttemptListener(serviceEvent -> {
            logger.info("### RECONNECTING ATTEMPT: "+serviceEvent);
        });
        messagingService.addReconnectionListener(serviceEvent -> {
            logger.info("### RECONNECTED: "+serviceEvent);
        });

        // build the publisher object, starts its own thread
        final PersistentMessagePublisher publisher = messagingService.createPersistentMessagePublisherBuilder()
                .build();
        publisher.start();

        ScheduledExecutorService statsPrintingThread = Executors.newSingleThreadScheduledExecutor();
        statsPrintingThread.scheduleAtFixedRate(() -> {
            System.out.printf("%s %s Published msgs/s: %,d%n",API,SAMPLE_NAME,msgSentCounter);  // simple way of calculating message rates
            msgSentCounter = 0;
        }, 1, 1, TimeUnit.SECONDS);

        System.out.println(API + " " + SAMPLE_NAME + " connected, and running. Press [ENTER] to quit.");
        System.out.println("Publishing to topic '"+ TOPIC_PREFIX + API.toLowerCase() +
                "/pers/pub/...', please ensure queue has matching subscription.");
        byte[] payload = new byte[PAYLOAD_SIZE];  // preallocate memory, for reuse, for performance
        Properties messageProps = new Properties();
        messageProps.put(SolaceProperties.MessageProperties.PERSISTENT_ACK_IMMEDIATELY, "true");  // TODO Remove when v1.1 API comes out
        // loop the main thread, waiting for a quit signal
        while (System.in.available() == 0 && !isShutdown) {
            OutboundMessageBuilder messageBuilder = messagingService.messageBuilder().fromProperties(messageProps);
            try {
                // each loop, change the payload, less trivial
                char chosenCharacter = (char)(Math.round(msgSentCounter % 26) + 65);  // rotate through letters [A-Z]
                Arrays.fill(payload,(byte)chosenCharacter);  // fill the payload completely with that char
                OutboundMessage message = messageBuilder.build(payload);  // binary payload message
                // dynamic topics!!
                String topicString = new StringBuilder(TOPIC_PREFIX).append(API.toLowerCase())
                        .append("/pers/pub/").append(chosenCharacter).toString();
                try {
                    // send the message
                    publisher.publishAwaitAcknowledgement(message, Topic.of(topicString), 2000L);  // wait up to 2 seconds for ACK
                    msgSentCounter++;  // add one
                } catch (PubSubPlusClientException e) {  // could be different types
                    logger.warn(String.format("NACK for Message %s - %s", message, e));
                } catch (InterruptedException e) {
                    // got interrupted by someone while waiting for my publish confirm?
                    logger.info("Got interrupted, probably shutting down",e);
                }
            } catch (RuntimeException e) {  // threw from send(), only thing that is throwing here, but keep trying (unless shutdown?)
                logger.warn("### Caught while trying to publisher.publish()",e);
                isShutdown = true;
            } finally {
                try {
                    Thread.sleep(1000 / APPROX_MSG_RATE_PER_SEC);  // do Thread.sleep(0) for max speed
                    // Note: STANDARD Edition Solace PubSub+ broker is limited to 10k msg/s max ingress
                } catch (InterruptedException e) {
                    isShutdown = true;
                }
            }
        }
        isShutdown = true;
        statsPrintingThread.shutdown();  // stop printing stats
        publisher.terminate(1500);
        messagingService.disconnect();
        System.out.println("Main thread quitting.");
    }

}

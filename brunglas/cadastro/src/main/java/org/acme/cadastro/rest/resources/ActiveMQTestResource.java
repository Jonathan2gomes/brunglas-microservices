//package org.acme.cadastro.rest.resources;
//
//import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
//import io.vertx.core.logging.Logger;
//import io.vertx.core.logging.LoggerFactory;
//import org.hibernate.internal.util.collections.CollectionHelper;
//import org.testcontainers.containers.GenericContainer;
//import org.testcontainers.containers.output.Slf4jLogConsumer;
//import org.testcontainers.containers.wait.strategy.Wait;
//import org.testcontainers.utility.TestcontainersConfiguration;
//
//import java.util.Map;
//
//public class ActiveMQTestResource implements QuarkusTestResourceLifecycleManager {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQTestResource.class);
//    private static final String ACTIVEMQ_IMAGE = "vromero/activemq-artemis:2.11.0-alpine";
//    private static final String ACTIVEMQ_USERNAME = "artemis";
//    private static final String ACTIVEMQ_PASSWORD = "simetraehcapa";
//    private static final int ACTIVEMQ_PORT = 61616;
//
//    private GenericContainer<?> container;
//
//    @Override
//    public Map<String, String> start() {
//        LOGGER.info(TestcontainersConfiguration.getInstance().toString());
//
//        try {
//            container = new GenericContainer<>(ACTIVEMQ_IMAGE)
//                    .withExposedPorts(ACTIVEMQ_PORT)
//                    .withLogConsumer(new Slf4jLogConsumer(LOGGER))
//                    .withEnv("BROKER_CONFIG_MAX_DISK_USAGE", "100")
//                    .waitingFor(Wait.forListeningPort());
//
//            container.start();
//
//            String brokerUrlTcp = String.format("tcp://127.0.0.1:%d", container.getMappedPort(ACTIVEMQ_PORT));
//            String brokerUrlWs = String.format("ws://127.0.0.1:%d", container.getMappedPort(ACTIVEMQ_PORT));
//
//            return CollectionHelper.mapOf(
//                    "quarkus.artemis.url", brokerUrlTcp,
//                    "quarkus.artemis.username", ACTIVEMQ_USERNAME,
//                    "quarkus.artemis.password", ACTIVEMQ_PASSWORD,
//                    "camel.component.paho.brokerUrl", brokerUrlTcp,
//                    "camel.component.paho.username", ACTIVEMQ_USERNAME,
//                    "camel.component.paho.password", ACTIVEMQ_PASSWORD,
//                    "broker-url.ws", brokerUrlWs);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void stop() {
//        try {
//            if (container != null) {
//                container.stop();
//            }
//        } catch (Exception e) {
//            // ignored
//        }
//    }
//}

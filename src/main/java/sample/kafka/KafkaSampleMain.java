package sample.kafka;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Commands:
 * <ul>
 *     <li>cd ~/kafka_2.13-3.5.0</li>
 *     <li>bin/zookeeper-server-start.sh config/zookeeper.properties</li>
 *     <li>bin/kafka-server-start.sh config/server.properties</li>
 *     <li>bin/kafka-topics.sh --bootstrap-server localhost:9092 --list</li>
 *     <li>bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group sample-group --describe</li>
 *     <li>bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group sample-group --reset-offsets --to-earliest --topic sample-topic --dry-run</li>
 *     <li>bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group sample-group --reset-offsets --to-earliest --topic sample-topic --execute</li>
 *     <li>bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group sample-group --delete-offsets --topic sample-topic</li>
 * </ul>
 */
class KafkaSampleMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSampleMain.class);

    public static void main(String[] args) {
        KafkaSampleConfiguration configuration = new KafkaSampleConfiguration();
        createTopic(configuration);
    }

    private static void createTopic(KafkaSampleConfiguration configuration) {
        CreateTopicsResult createdTopic;
        try (Admin admin = Admin.create(configuration.getCommonProperties())) {
            createdTopic = admin.createTopics(List.of(
                    new NewTopic(configuration.getTopicName(), 3/*numPartitions*/, (short) 1/*replicationFactor*/)
            ));
        }
        try {
            createdTopic.all().get();
            LOGGER.info("Topic has been created.");
        } catch (Exception e) {
            if (e.getCause() instanceof TopicExistsException) {
                LOGGER.info("Topic has been created before.");
            } else {
                LOGGER.info("Couldn't create topic.", e);
            }
        }
    }
}

package sample.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static io.qala.datagen.RandomShortApi.Long;
import static io.qala.datagen.RandomShortApi.alphanumeric;

class KafkaSampleProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSampleProducer.class);
    private final KafkaSampleConfiguration configuration;
    private final KafkaProducer<Long, String> producer;

    public static void main(String[] args) {
        KafkaSampleProducer producer = new KafkaSampleProducer();
        producer.produce(2);
    }

    private KafkaSampleProducer() {
        configuration = new KafkaSampleConfiguration();
        producer = new KafkaProducer<>(configuration.getProducerProperties());
    }

    private void produce(int numberOfEvents) {
        for (int i = 0; i < numberOfEvents; i++) {
            long key = Long(0, 2);
            String value = "%s_%s".formatted(alphanumeric(10), LocalDateTime.now());
            producer.send(new ProducerRecord<>(configuration.getTopicName(), key, value), (RecordMetadata metadata, Exception exception) -> {
                if (exception != null) {
                    LOGGER.info("Couldn't send a message.", exception);
                } else {
                    LOGGER.info("Sent a message: key={}, value={}, metadata={}.", key, value, metadata);
                }
            });
        }
        producer.flush();
    }
}

package sample.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

class KafkaSampleConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSampleConsumer.class);
    private final KafkaSampleConfiguration configuration;
    private final KafkaConsumer<Long, String> consumer;

    public static void main(String[] args) {
        KafkaSampleConsumer consumer = new KafkaSampleConsumer();
        consumer.consume();
    }

    private KafkaSampleConsumer() {
        configuration = new KafkaSampleConfiguration();
        consumer = new KafkaConsumer<>(configuration.getConsumerProperties());
    }

    private void consume() {
        Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutdown is detected.");
            consumer.wakeup();

            try {
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        try {
            consumer.subscribe(List.of(configuration.getTopicName()));
            //noinspection InfiniteLoopStatement
            while (true) {
                ConsumerRecords<Long, String> records = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<Long, String> record : records) {
                    LOGGER.info("Received a message: key={}, value={}.", record.key(), record.value());
                }
            }
        } catch (WakeupException e) {
            LOGGER.info("Graceful shutdown of the consumer is started.");
        } catch (Exception e) {
            LOGGER.error("Unexpected exception in the consumer.", e);
        } finally {
            consumer.close();//consumer offsets will also be committed
            LOGGER.info("Graceful shutdown of the consumer is finished.");
        }
    }
}

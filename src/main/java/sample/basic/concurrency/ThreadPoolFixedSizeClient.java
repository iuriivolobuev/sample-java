package sample.basic.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ThreadPoolFixedSizeClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolFixedSizeClient.class);

    public static void main(String[] args) {
        ThreadPoolFixedSize threadPool = new ThreadPoolFixedSize(2);
        threadPool.execute(task("task-01", 3));
        threadPool.execute(task("task-02", 5));
        threadPool.execute(task("task-03", 3));
        threadPool.shutdown();
    }

    private static Runnable task(String task, int seconds) {
        return () -> {
            LOGGER.info("Starting task: {}.", task);
            try {
                Thread.sleep(seconds * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info("Finishing task: {}.", task);
        };
    }
}

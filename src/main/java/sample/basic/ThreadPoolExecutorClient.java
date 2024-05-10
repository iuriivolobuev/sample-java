package sample.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ThreadPoolExecutorClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolExecutorClient.class);

    public static void main(String[] args) throws InterruptedException {
        //noinspection resource
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> runWithSleep("task-01", 3));
        executorService.submit(() -> runWithSleep("task-02", 5));

        executorService.shutdown();//initiates shutdown, doesn't block
        boolean afterAwait = executorService.awaitTermination(10, TimeUnit.SECONDS);//blocks until all tasks are completed or timeout occurs
        LOGGER.info("[all-tasks-were-completed={}, timeout-occurred={}]", afterAwait, !afterAwait);
    }

    private static void runWithSleep(String task, int seconds) {
        LOGGER.info("Starting task: {}.", task);
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("Finishing task: {}.", task);
    }
}

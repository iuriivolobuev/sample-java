package sample.basic.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ThreadPoolClients {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolClients.class);

    public static void main(String[] args) {
        runStandardExecutor();
        runCustomExecutor();
    }

    private static void runStandardExecutor() {
        try (ExecutorService executorService = Executors.newFixedThreadPool(1)) {
            executorService.submit(task("task1", 100));
            executorService.submit(task("task2", 200));

            executorService.shutdown();//initiates shutdown, doesn't block

            boolean completed = executorService.awaitTermination(150, TimeUnit.MILLISECONDS);//blocks until all tasks are completed or timeout occurs
            if (completed) {
                LOGGER.info("all tasks completed");
            } else {
                executorService.shutdownNow();
                LOGGER.warn("timeout elapsed, shut down uncompleted tasks");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void runCustomExecutor() {
        ThreadPoolFixedSize threadPool = new ThreadPoolFixedSize(2);
        threadPool.execute(task("task1", 100));
        threadPool.execute(task("task2", 200));
        threadPool.execute(task("task3", 100));
        threadPool.shutdown();
    }

    private static Runnable task(String task, long ms) {
        return () -> {
            LOGGER.info("[{}] started", task);
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LOGGER.info("[{}] completed", task);
        };
    }
}

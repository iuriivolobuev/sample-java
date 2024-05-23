package sample.basic.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class ThreadPoolFixedSize {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolFixedSize.class);

    private final BlockingQueue<Runnable> tasks;
    private volatile boolean stopped = false;

    public ThreadPoolFixedSize(int size) {
        this.tasks = new LinkedBlockingQueue<>();
        for (int i = 0; i < size; i++)
            new Worker().thread.start();
    }

    public void execute(Runnable command) {
        if (this.stopped)
            throw new RuntimeException("Not allowed to add tasks after shutdown.");
        boolean offered = this.tasks.offer(command);
        if (!offered)
            throw new RuntimeException("Couldn't add task to queue.");
    }

    public void shutdown() {
        this.stopped = true;
    }

    private class Worker implements Runnable {
        private final Thread thread;

        private Worker() {
            this.thread = new Thread(this);
        }

        @Override public void run() {
            try {
                for (;;) {
                    if (stopped && tasks.isEmpty())
                        break;

                    Runnable task = tasks.poll(1, TimeUnit.SECONDS);
                    if (task != null)
                        task.run();
                }
            } catch (InterruptedException e) {
                LOGGER.info("Thread {} was interrupted.", this.thread);
            }
        }
    }
}

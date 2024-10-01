package sample.basic.concurrency;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Tomcat:
 * - maxConnections = number of connections that are being processed by servlets + those that were just accepted
 * - maxConnections + acceptCount (backlog), additional connections will be refused
 *
 * <p>Timeouts:
 * - connection timeout (before accept if wasn't accepted during some time)
 * - socket timeout (after accept if was idle during some time)
 *
 * <p>Example:
 * - in server.xml: maxThreads = 5, maxConnections = 6, acceptCount = 10
 */
class TomcatClient {
    public static void main(String[] args) {
        int threadCount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            int connectionIndex = i;
            executorService.execute(() -> {
                try {
                    new URL("http://localhost:8080/with-delay").openConnection().getInputStream();
                    System.out.println(String.format("Connection [%d] was successfully performed.", connectionIndex));
                } catch (IOException e) {
                    System.out.println(String.format("Connection [%d] was failed: ", connectionIndex) + e.getMessage());
                }
            });
        }
        executorService.shutdown();
    }
}

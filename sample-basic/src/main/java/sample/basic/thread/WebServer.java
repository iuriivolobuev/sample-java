package sample.basic.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import static io.qala.datagen.RandomShortApi.alphanumeric;

class WebServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebServer.class);

    //after start open something like "http://localhost:8080/hello" in browser (or just "curl localhost:8080/hello")
    public static void main(String[] args) throws IOException {
        ThreadPoolFixedSize threadPool = new ThreadPoolFixedSize(2);
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.execute(new ProcessRequestTask(socket));
            }
        }
    }

    private static class ProcessRequestTask implements Runnable {
        private final String connName = alphanumeric(10);
        private final Socket socket;

        public ProcessRequestTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                long ms = 3000;
                LOGGER.info("[{}]: sleeping for {} ms", connName, ms);
                Thread.sleep(ms);
                LOGGER.info("[{}]: awake", connName);

                BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                String reqInfo = reader.readLine();
                //example: reqInfo="GET /hello HTTP/1.1"
                LOGGER.info("[{}]: reqInfo=\"{}\"", connName, reqInfo);
                String value = reqInfo.split(" ")[1].replace("/", "");
                String response = "HTTP/1.1 200 OK\r\n\r\n<h1>" + value + "</h1>";
                socket.getOutputStream().write(response.getBytes());

                socket.getOutputStream().close();
                socket.close();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

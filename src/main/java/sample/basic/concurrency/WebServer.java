package sample.basic.concurrency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class WebServer {
    //after start open something like: http://localhost:8080/hello
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
        private final Socket socket;

        public ProcessRequestTask(Socket socket) {
            this.socket = socket;
        }

        @Override public void run() {
            try {
                Thread.sleep(3000);
                System.out.println("After sleep");
                BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                String value = reader.readLine().split(" ")[1].replace("/", "");
                String response = "HTTP/1.0 200 OK\r\n\r\n<h1>" + value + "</h1>";
                socket.getOutputStream().write(response.getBytes());
                socket.getOutputStream().close();
                socket.close();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

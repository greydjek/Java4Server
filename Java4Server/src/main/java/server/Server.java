package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Server {
    private final int PORT = 8089;
    private ServerSocket serverSocket;
    private ConcurrentLinkedDeque<Handler> client;

    public Server() throws IOException {
        client = new ConcurrentLinkedDeque<>();
        try {

            serverSocket = new ServerSocket(PORT);
            System.out.println("Server start... ");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connect");
                Handler handler = new Handler(socket, this);
                client.add(handler);
                new Thread(handler).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) throws IOException {
        new Server();
    }
}

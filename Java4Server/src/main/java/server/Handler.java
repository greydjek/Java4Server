package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class Handler implements Runnable {

    private String userName;
    private int counter = 0;
    private Socket socket;
    private final Server server;
    private DataOutputStream dos;
    private DataInputStream dis;
    private SimpleDateFormat format;

    public Handler(Socket socket, Server server) throws IOException {
        counter++;
        userName = "User#" + counter;
        this.socket = socket;
        this.server = server;
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        format = new SimpleDateFormat();
        format = new SimpleDateFormat("dd.MM.yyyy hh.mm.ss");
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = dis.readUTF();

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(" Connection is lost ");
        }
    }

    public String getTime() {
        // LocalDateTime now = LocalDateTime.now();
        return format.format(new Date());
    }

    public String getMessage(String message) {
        return getTime() + "[" + userName + "]" + message;
    }

    public void sendMessage(String message) throws IOException {
        dos.writeUTF(getMessage(message));
        dos.flush();
    }
}

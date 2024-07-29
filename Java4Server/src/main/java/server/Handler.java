package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class Handler implements Runnable {
    private Path root;
    private Path userDir;
    private String userName;
    private int counter = 0;
    private Socket socket;
    private final Server server;
    private DataOutputStream dos;
    private DataInputStream dis;
    private SimpleDateFormat format;

    public Handler(Socket socket, Server server) throws IOException {
        root = Paths.get("C:\\Education\\Java4\\Java4Server\\Java4Server\\Server");
        if (!Files.exists(root)) {
            Files.createDirectory(root);
        }
        counter++;
        userName = "User#" + counter;
        userDir = root.resolve(userName);
        if (!Files.exists(userDir)) {
            Files.createDirectory(userDir);
        }
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
                String fileName = dis.readUTF();
                long size = dis.readLong();
   Path path = userDir.resolve(fileName);
    Files.copy(dis, path, StandardCopyOption.REPLACE_EXISTING);
    responseOk();
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

    private void responseOk() throws IOException {
        dos.writeUTF(getMessage("Response OK "));
        dos.flush();

    }

    public void sendMessage(String message) throws IOException {
        dos.writeUTF(getMessage(message));
        dos.flush();
    }
}

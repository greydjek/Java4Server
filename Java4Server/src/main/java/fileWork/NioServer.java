package fileWork;

import com.sun.source.tree.WhileLoopTree;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NioServer {
    private ByteBuffer byteBuffer = ByteBuffer.allocate(256);
    private final Path DIRECTORY = Path.of("C:\\Education\\Java4\\Java4Server\\Java4Server\\Server");
    private List<String> listFiles;
    String[] command;
    Selector selector;
    ServerSocketChannel serverSocketChannel;

    public NioServer() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        selector = Selector.open();
        serverSocketChannel.bind(new InetSocketAddress(8189));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (serverSocketChannel.isOpen()) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    handleAccept(key);
                }
                if (key.isReadable()) {
                    reedHandler(key);
                }
                iterator.remove();
            }
        }
    }

    private void reedHandler(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        StringBuilder sb = new StringBuilder();
        while (true) {
            int reed = channel.read(byteBuffer);
            if (reed == -1) {
                channel.close();
                return;
            }
            if (reed == 0) {
                break;
            }
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                sb.append((char) byteBuffer.get());
                command = sb.toString().split(" ");
            }
        }
       byteBuffer.clear();
        switch (command[0]) {
            case "ls":
            listreturn();
            for (String l : listFiles) {
                System.out.println(l +" ");
                    String s = "[" + l + "] ";
                    channel.write(ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8)));
            }
            byteBuffer.clear();
            break;
            case "cat":
            String nameF = "";
            String pathFile;
            byteBuffer.clear();
            listreturn();
            File f;
            System.out.println(command.length);
            for (int i = 0; i < command.length; i++) {
                System.out.println(command[i]);
            }
                if (sb.toString().contains(" ")) {
                    for (String name : listFiles) {
                        if (command[1].equals(name)){
                            System.out.println(name);
                            nameF= name;
                        }
                    }
                if (command[1].equals(nameF)) {
                    System.out.println("open file " + command[1]);
                    pathFile = String.valueOf(DIRECTORY.resolve(nameF));
                    f = new File(pathFile);
                    try (InputStream fis = new FileInputStream(f)) {
                        byte[] bytes = new byte[(int) f.length()];
                            fis.read(bytes);
                            String file = new String(bytes, StandardCharsets.UTF_8);
                            channel.write(ByteBuffer.wrap(file.getBytes(StandardCharsets.UTF_8)));
                            System.out.println(file);
                        }
                } else {
                    String s = "File not found";
                    channel.write(ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8)));
                }


            } else {
                String s = "not found command";
                    channel.write(ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8)));
            }
            break;
            default:      String resault = "[From server] " + sb.toString();
                channel.write(ByteBuffer.wrap(resault.getBytes(StandardCharsets.UTF_8)));
            break;
        }




    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
    }

    private List listreturn() throws IOException {
        return listFiles = Files.list(DIRECTORY).map(p -> p.getFileName().
                toString()).collect(Collectors.toList());
    }

//    private void handleCommand() throws IOException {
//        //if (message.equals("ls")){
//        listFiles = (ArrayList<String>) Files.list(DIRECTORY).map(path -> path.getFileName().toString())
//                .collect(Collectors.toList());
//        for (int i = 0; i < listFiles.size(); i++) {
//             byteBuffer.put(listFiles.get(i).getBytes());
//             //  }
//        }
//    }

    public static void main(String... args) throws IOException {
        new NioServer();
    }
}

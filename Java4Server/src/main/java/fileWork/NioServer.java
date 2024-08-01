package fileWork;

import java.awt.*;
import java.io.IOException;
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
import java.util.Set;
import java.util.stream.Collectors;

public class NioServer {
    private ByteBuffer byteBuffer = ByteBuffer.allocate(256);
    private String command;
    private final Path DIRECTORY = Path.of("C:\\Education\\Java4\\Java4Server\\Java4Server\\Server");
    private ArrayList<String> listFiles;
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
                command = byteBuffer.toString();
                switch (sb.toString()) {
                    case "ls":
                        handleCommand();
                        byteBuffer.clear();
                        String s= (String) Files.list(DIRECTORY).collect(Collectors.toList()).toArray().toString();
                        channel.write(ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8)) );

                        break;
                    case "cat":
                        break;
                          }
            }
            byteBuffer.clear();
        }
        String resault = "[From server] " + sb.toString();
        channel.write(ByteBuffer.wrap(resault.getBytes(StandardCharsets.UTF_8)));

    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void handleCommand() throws IOException {
        //if (message.equals("ls")){
        listFiles = (ArrayList<String>) Files.list(DIRECTORY).map(path -> path.getFileName().toString())
                .collect(Collectors.toList());
        for (int i = 0; i < listFiles.size(); i++) {
            byteBuffer.put(listFiles.get(i).getBytes());
            //  }
        }
    }

    public static void main(String... args) throws IOException {
        new NioServer();
    }
}

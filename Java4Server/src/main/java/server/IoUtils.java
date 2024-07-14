package server;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class IoUtils {
    public static void main(String... args) throws IOException {
        String c = "copy1.txt";
        //String resurses = IoUtils.class.getResource("1.txt").getFile();
        InputStream in = new FileInputStream("C:/Education/Java4/Java4Server/Java4Server/src/main/resources/comResurses/dir1/1.txt");

        OutputStream outputStream = new FileOutputStream("C:/Education/Java4/Java4Server/Java4Server" +
                "/src/main/resources/comResurses/dir1/"+c);
        byte[] buffer = new byte[8000];
        int readBytes = 0;
        while (true) {
            readBytes = in.read(buffer);
            if (readBytes == -1) {
                break;
            }
            outputStream.write(buffer, 0, readBytes);
        }
    }
}

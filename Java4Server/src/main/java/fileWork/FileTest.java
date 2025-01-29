package fileWork;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.Path.of;

public class FileTest {
    public static void main(String... args) throws IOException {
        Files.writeString(Path.of("C:\\Education\\Java4\\Java4Server\\Java4Server\\Server",
                        "1.txt")
                , " 111222 hello Russia ", StandardOpenOption.APPEND);
    }
}

package fileWork;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Path;

public class BufferWorking {
    public static void main(String... args){
         final Path DIRECTORY = Path.of("C:\\Education\\Java4\\Java4Server\\Java4Server\\Server");
        String name = "1.txt";
         System.out.println(DIRECTORY.resolve(name));
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        byteBuffer.put((byte) 'a');
        byteBuffer.put((byte) 'b');
        byteBuffer.put((byte) 'c');
byteBuffer.flip();
   while (byteBuffer.hasRemaining()){
       System.out.println((char) byteBuffer.get());
   }
   byteBuffer.rewind();
        while (byteBuffer.hasRemaining()){
            System.out.println((char) byteBuffer.get());
        }
        while (byteBuffer.hasRemaining()){
            System.out.println((char) byteBuffer.get());
        }
    }
}

package fileWork;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class BufferWorking {
    public static void main(String... args){
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

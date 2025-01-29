package netty;

import lombok.Builder;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import messageWorker.AbstractMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import messageWorker.FileMessage;
import messageWorker.FileRequest;
import messageWorker.ListMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.ScatteringByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
    private Path serverDir;
    private byte[] byffer;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//содать папку при регистрации пользователя
        byffer = new byte[8192];
        serverDir = Paths.get("C:\\Projects\\repositoryClientServerNetty\\Java4Server\\Java4Server\\server");
        ctx.writeAndFlush(new ListMessage(serverDir));
        log.debug("client connect...");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractMessage msg) throws Exception {
        switch (msg.getCommand()) {
            case FILE_MESSAGE:
                processFile((FileMessage) msg, ctx);
                break;
            case FILE_REQUEST:
                sendFile((FileRequest) msg, ctx);
                break;
            case AUTENTIFCATION_MESSAGE:
                autentificatioUser(msg, ctx);
                break;
            case LIST_MESSAGE:
                ctx.writeAndFlush(new ListMessage(serverDir));
                break;
        }
    }

    private void autentificatioUser(AbstractMessage msg, ChannelHandlerContext ctx) {
    List<String> userNamePass =  Arrays.stream(msg.toString().split("@")).collect(Collectors.toList());

    }

    private void sendFile(FileRequest msg, ChannelHandlerContext ctx) throws IOException {
        boolean isFirstBatch = true, isFinishBatch;
        Path filePash = serverDir.resolve(msg.getName());
        long size = Files.size(filePash);
        try (FileInputStream is = new FileInputStream(serverDir.resolve(msg.getName()).toFile())) {
            int read;
            while ((read = is.read(byffer)) != -1) {
                isFinishBatch = (is.available() <= 0);
FileMessage message = new FileMessage(filePash, byffer, msg.getName().toString(), isFirstBatch, isFinishBatch,read);
              isFirstBatch = false;
                ctx.writeAndFlush(message);
            }
        } catch (Exception e) {
        }
    }

    private void processFile(FileMessage msg, ChannelHandlerContext ctx) throws IOException {
        Path file = serverDir.resolve(msg.getName());
if (msg.isFirstButch()){
    Files.deleteIfExists(file);
}
try (FileOutputStream fos = new FileOutputStream(file.toFile(), true)){
    fos.write(msg.getBytes(),0, msg.getEndByteNum());
}
    }
}

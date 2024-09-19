package netty;

import lombok.Builder;
import messageWorker.AbstractMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import messageWorker.FileMessage;
import messageWorker.FileRequest;
import messageWorker.ListMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.ScatteringByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Builder
public class MessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
    private Path serverDir;
    private byte[] byffer;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//содать папку при регистрации пользователя
        serverDir = Paths.get("Server");
        ctx.writeAndFlush(new ListMessage(serverDir));
        byffer = new byte[8192];
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
        }
    }

    private void sendFile(FileRequest msg, ChannelHandlerContext ctx) throws IOException {
        boolean isFirstBatch = true;
        Path filePash = serverDir.resolve(msg.getName());
        long size = Files.size(filePash);
        try (FileInputStream is = new FileInputStream(serverDir.resolve(msg.getName()).toFile())) {
            int read;
            while ((read = is.read(byffer)) != -1) {
                FileMessage message = FileMessage.builder()
                                .bytes(byffer)
                                .size(size)
                                .isFirstButch(isFirstBatch)
                                .isFinishBatch(is.available() <= 0)
                                .endByteNum(read)
                                .build();
                isFirstBatch = false;
                ctx.writeAndFlush(message);
            }
        } catch (Exception e) {
        }
    }

    private void processFile(FileMessage msg, ChannelHandlerContext ctx) {
        Path file = serverDir.resolve(msg.getName());

    }
}

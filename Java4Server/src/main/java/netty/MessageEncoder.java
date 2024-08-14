package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
@Slf4j
public class MessageEncoder extends ChannelOutboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(MessageEncoder.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String message = (String) msg;
        log.debug("recived {}", msg);
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(message.getBytes(StandardCharsets.UTF_8));
        buffer.retain();
        ctx.writeAndFlush(buffer);
    }
}

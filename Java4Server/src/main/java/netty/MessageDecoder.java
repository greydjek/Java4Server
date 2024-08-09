package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
log.debug("Client connect...");

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf= (ByteBuf) msg;
        log.debug("{}", msg);
    StringBuilder sb = new StringBuilder();
    while (byteBuf.isReadable()){
        char c = (char) byteBuf.readByte();
        sb.append(c);
    }
    String message = sb.toString();
    log.debug("reserved: {} ", message );
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client disconnected...");
    }
}

package netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(StringHandler.class);
private SimpleDateFormat format;

    public StringHandler() {
        format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
String str = (String) msg;
log.debug("received: {}", msg );
String resalt = "["+ format.format(new Date()) + "]" + str;
ctx.write(resalt);
    }
}

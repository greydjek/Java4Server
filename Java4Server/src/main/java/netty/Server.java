package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {
    public Server(){
        EventLoopGroup auth = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(auth,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                        protected void initChannel(SocketChannel channel) throws Exception{

                    }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(8189).sync();
            log.debug("Server started...");
channelFuture.channel().closeFuture().sync();// block
        }catch (Exception e){
            e.printStackTrace();
        }finally {
auth.shutdownGracefully();
worker.shutdownGracefully();
        }
    }
    public static void main (String... args){
        new Server();
    }
}

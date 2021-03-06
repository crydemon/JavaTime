package wesocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;
import java.net.InetSocketAddress;

/**
 * @ClassName ChatServer
 * @Description TODO
 * @Author wqkant
 * @Date 2020/8/2 17:11
 * @Version 1.0
 */
public class ChatServer {

  private final ChannelGroup channelGroup = new DefaultChannelGroup(
      ImmediateEventExecutor.INSTANCE);
  private final EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
  private final EventLoopGroup workerLoopGroup = new NioEventLoopGroup(2);
  private Channel channel;

  public ChannelFuture start(InetSocketAddress address) {
    System.out.println("start");
    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(bossLoopGroup, workerLoopGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(createInitializer(channelGroup));
    ChannelFuture future = bootstrap.bind(address);
    future.syncUninterruptibly();
    channel = future.channel();
    return future;
  }

  protected ChannelInitializer<Channel> createInitializer(ChannelGroup group) {
    System.out.println("createInitializer");
    return new ChatServerInitializer(group);
  }

  public void destroy() {
    if (channel != null) {
      channel.close();
    }
    channelGroup.close();
    workerLoopGroup.shutdownGracefully();
    bossLoopGroup.shutdownGracefully();
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("please give port");
      System.exit(1);
    }
    int port = Integer.parseInt(args[0]);
    System.out.println(port);
    final ChatServer endpoint = new ChatServer();
    ChannelFuture future = endpoint.start(new InetSocketAddress(port));
    Runtime.getRuntime().addShutdownHook(new Thread(() -> endpoint.destroy()));
    future.channel().closeFuture().syncUninterruptibly();
  }

}

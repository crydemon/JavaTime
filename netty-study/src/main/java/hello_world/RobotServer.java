package hello_world;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Date;

public class RobotServer {


    public static void main(String[] args) throws InterruptedException {
        RobotServer.bind(8080);
    }

    private static void bind(int port) {
        // 用于服务端接受客户端连接bossGroup
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        // 用于进行SocketChannel的网络读写
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new SimpleChannelInboundHandler() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object command) throws Exception {
                                    // 读取指令
                                    ByteBuf byteBuf = (ByteBuf) command;
                                    byte[] req = new byte[byteBuf.readableBytes()];
                                    byteBuf.readBytes(req);
                                    // 打印读取的内容
                                    System.out.println("Robot Server receive a command: " + new String(req, "UTF-8"));
                                    // 处理指令
                                    String result = "hello,你好!我叫Robot。";
                                    // 将消息先放到缓冲数组中, 再全部发送到SocketChannel中
                                    ByteBuf resp = Unpooled.copiedBuffer(result.getBytes());
                                    channelHandlerContext.writeAndFlush(resp);
                                }
                            });
                        }
                    });
            // 绑定端口, 同步等待成功
            ChannelFuture future = bootstrap.bind(port).sync();
            // 等待服务端端口关闭
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅关闭线程池资源
            parentGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}


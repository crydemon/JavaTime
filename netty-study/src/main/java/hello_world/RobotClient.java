package hello_world;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


class RobotClient {

    public void connect(int port, String host) throws InterruptedException {
        // 配置客户端NIO线程组, 用于客户端I/O读写
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new SimpleChannelInboundHandler() {

                                        @Override
                                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
                                            ByteBuf byteBuf = (ByteBuf) msg;
                                            byte[] response = new byte[byteBuf.readableBytes()];
                                            byteBuf.readBytes(response);

                                            System.out.println("Client: " + new String(response, "UTF-8"));
                                        }

                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            final ByteBuf firstMsg;
                                            byte[] req = "hello".getBytes();
                                            firstMsg = Unpooled.buffer(req.length);
                                            firstMsg.writeBytes(req);
                                            ctx.channel().writeAndFlush(firstMsg);
                                            super.channelActive(ctx);
                                        }

                                    });
                        }
                    });

            // 发起异步连接
            ChannelFuture future = bootstrap.connect(host, port);
            // 等待客户端关闭
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new RobotClient().connect(8080, "127.0.0.1");
    }
}

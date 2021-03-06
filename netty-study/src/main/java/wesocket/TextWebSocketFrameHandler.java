package wesocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @ClassName TextWebSocketFrameHandler
 * @Description TODO
 * @Author wqkant
 * @Date 2020/8/2 16:48
 * @Version 1.0
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

  private final ChannelGroup group;

  public TextWebSocketFrameHandler(ChannelGroup group) {
    this.group = group;
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    System.out.println("-------------------");
    if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
      System.out.println(evt.getClass());
      ctx.pipeline().remove(HttpRequestHandler.class);
      ctx.writeAndFlush(new TextWebSocketFrame(
          "Client " + ctx.channel() + " joined"));
      group.add(ctx.channel());
    } else {
      super.userEventTriggered(ctx, evt);
    }
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
    ctx.writeAndFlush(msg.retain());
  }
}

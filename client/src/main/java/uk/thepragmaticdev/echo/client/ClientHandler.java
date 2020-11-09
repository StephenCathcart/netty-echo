package uk.thepragmaticdev.echo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;
import io.netty.channel.ChannelHandlerContext;

@Sharable
public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

  @Override
  public void channelActive(ChannelHandlerContext context) throws Exception {
    context.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
  }

  @Override
  public void channelRead0(ChannelHandlerContext context, ByteBuf in) throws Exception {
    System.out.println(String.format("client received: %s", in.toString(CharsetUtil.UTF_8)));
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
    cause.printStackTrace();
    context.close();
  }
}

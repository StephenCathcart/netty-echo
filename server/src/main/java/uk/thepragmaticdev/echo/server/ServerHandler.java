package uk.thepragmaticdev.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;

@Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext context, Object msg) {
    var in = (ByteBuf) msg;
    System.out.println(String.format("server received: %s", in.toString(CharsetUtil.UTF_8)));
    context.write(in);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext context) {
    context.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
    cause.printStackTrace();
    context.close();
  }
}

package uk.thepragmaticdev.echo.server;

import java.net.InetSocketAddress;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

  private final int port;

  public Server(int port) {
    this.port = port;
  }

  public void start() throws Exception {
    var group = new NioEventLoopGroup();
    try {
      var bootstrap = new ServerBootstrap();
      bootstrap.group(group) //
          .channel(NioServerSocketChannel.class) //
          .localAddress(new InetSocketAddress(port)) //
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel channel) throws Exception {
              channel.pipeline().addLast(new ServerHandler());
            }
          });
      var future = bootstrap.bind().sync();
      System.out.println(String.format("started and listening for connections on %s", future.channel().localAddress()));
      future.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully().sync();
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println(String.format("Usage: %s <port>", Server.class.getSimpleName()));
      return;
    }
    final var port = Integer.parseInt(args[0]);
    new Server(port).start();
  }
}
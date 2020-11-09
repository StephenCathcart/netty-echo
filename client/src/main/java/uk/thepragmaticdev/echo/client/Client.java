package uk.thepragmaticdev.echo.client;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

  private final String host;
  private final int port;

  public Client(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public void start() throws Exception {
    var group = new NioEventLoopGroup();
    try {
      var bootstrap = new Bootstrap();
      bootstrap.group(group) //
          .channel(NioSocketChannel.class) //
          .remoteAddress(new InetSocketAddress(host, port)) //
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel channel) throws Exception {
              channel.pipeline().addLast(new ClientHandler());
            }
          });
      var future = bootstrap.connect().sync();
      future.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully().sync();
    }

  }

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println(String.format("Usage: %s <host> <port>", Client.class.getSimpleName()));
      return;
    }
    final var host = args[0];
    final var port = Integer.parseInt(args[1]);
    new Client(host, port).start();
  }
}
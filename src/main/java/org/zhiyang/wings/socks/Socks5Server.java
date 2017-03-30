package org.zhiyang.wings.socks;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhiyang.wings.socks.handler.CloseHandle;
import org.zhiyang.wings.socks.handler.v5.Socks5CommandRequestHandler;
import org.zhiyang.wings.socks.handler.v5.Socks5InitialRequestHandler;

/**
 * @author lizhiyang.
 */
public class Socks5Server implements SocksServer {

    private final Logger log = LoggerFactory.getLogger(Socks5Server.class);

    private final SocksServerConfig socksServerConfig;

    public Socks5Server(SocksServerConfig socksServerConfig) {
        this.socksServerConfig = socksServerConfig;
    }


    @Override
    public void start() {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup(socksServerConfig.getServerSelectorThreads());

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, false)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            ChannelPipeline pipeline =  socketChannel.pipeline();
                            if (socksServerConfig.isSslForServer()) {
                                pipeline.addLast(socksServerConfig.getSslContextForServer().
                                        newHandler(socketChannel.alloc()));
                            }

                            pipeline.addLast(new IdleStateHandler(
                                    0,
                                    0,
                                    Socks5Server.this.socksServerConfig.getConnectionIdleTimeSeconds()));
                            pipeline.addLast(new CloseHandle(null));
                            pipeline.addLast(Socks5ServerEncoder.DEFAULT);
                            pipeline.addLast(new Socks5InitialRequestDecoder());
                            pipeline.addLast(new Socks5InitialRequestHandler(socksServerConfig));
                            pipeline.addLast(new Socks5CommandRequestDecoder());
                            pipeline.addLast(new Socks5CommandRequestHandler(socksServerConfig));

                        }

                    });

            serverBootstrap.bind(socksServerConfig.getPort()).sync();

            log.info("socks server listen port:{}", socksServerConfig.getPort());

        } catch (InterruptedException e) {

            log.error("start socks5 server fail!", e);

        }

    }
}

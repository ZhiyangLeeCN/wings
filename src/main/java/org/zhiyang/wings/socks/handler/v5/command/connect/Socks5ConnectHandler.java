package org.zhiyang.wings.socks.handler.v5.command.connect;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.socksx.v5.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhiyang.wings.$;
import org.zhiyang.wings.socks.SocksServerConfig;
import org.zhiyang.wings.socks.handler.DirectHandler;
import org.zhiyang.wings.socks.handler.CloseHandle;
import org.zhiyang.wings.socks.handler.ReplyHandler;

/**
 * @author lizhiyang.
 */
public class Socks5ConnectHandler extends SimpleChannelInboundHandler<Socks5CommandRequest> {

    private final Logger log = LoggerFactory.getLogger(Socks5ConnectHandler.class);

    private final Bootstrap bootstrap = new Bootstrap();

    private final SocksServerConfig socksServerConfig;

    public Socks5ConnectHandler(SocksServerConfig socksServerConfig) {
        this.socksServerConfig = socksServerConfig;
    }

    private void initBootstrap(ChannelHandlerContext ctx, Bootstrap bootstrap)
    {
        bootstrap.group(ctx.channel().eventLoop())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        this.socksServerConfig.getConnectTimeOutMillis())
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_REUSEADDR, true);
    }

    protected void socksV5Dispatch(ChannelHandlerContext ctx, Promise<Channel> promise,
                                   Socks5CommandRequest request)
    {
        initBootstrap(ctx, bootstrap);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {

                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new CloseHandle(ctx.channel()));
                pipeline.addLast(Socks5ClientEncoder.DEFAULT);
                pipeline.addLast(new SendSocks5InitialRequest(socksServerConfig));
                pipeline.addLast(new Socks5InitialResponseDecoder());
                pipeline.addLast(new Socks5InitialResponseHandler(socksServerConfig));
                pipeline.addLast(new SendSocks5ConnectCommand(socksServerConfig, request));
                pipeline.addLast(new Socks5CommandResponseDecoder());
                pipeline.addLast(new Socks5CommandResponseHandler(socksServerConfig));
                pipeline.addLast(new ClientDirectHandler(promise));

            }

        });
        bootstrap.connect(
                socksServerConfig.getDispatchSocksv5Address(),
                socksServerConfig.getDispatchSocksv5Port()).addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {

                if (!future.isSuccess()) {

                    ctx.writeAndFlush(new DefaultSocks5CommandResponse(
                            Socks5CommandStatus.FAILURE,
                            request.dstAddrType())
                    );

                }

            }

        });

    }

    protected void normalDispatch(ChannelHandlerContext ctx, Promise<Channel> promise,
                                  Socks5CommandRequest request)
    {
        initBootstrap(ctx, bootstrap);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new CloseHandle(ctx.channel()));
                pipeline.addLast(new DirectHandler(promise));
            }
        });
        bootstrap.connect(request.dstAddr(), request.dstPort()).addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {

                if (!future.isSuccess()) {

                    ctx.writeAndFlush(new DefaultSocks5CommandResponse(
                            Socks5CommandStatus.FAILURE,
                            request.dstAddrType())
                    );

                }

            }

        });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Socks5CommandRequest msg) throws Exception {

        final Socks5CommandRequest request = msg;

        Promise<Channel> promise = ctx.executor().newPromise();
        promise.addListener(new GenericFutureListener<Future<Channel>>() {

            @Override
            public void operationComplete(Future<Channel> future) throws Exception {
                final Channel outboundChannel = future.getNow();
                if (future.isSuccess()) {

                    ChannelFuture channelFuture = ctx.writeAndFlush(new DefaultSocks5CommandResponse(
                            Socks5CommandStatus.SUCCESS,
                            request.dstAddrType(),
                            request.dstAddr(),
                            request.dstPort())
                    );

                    channelFuture.addListener(new ChannelFutureListener() {

                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            ctx.pipeline().addLast(new ReplyHandler(outboundChannel));
                            ctx.pipeline().remove(Socks5ConnectHandler.this);
                            outboundChannel.pipeline().addLast(new ReplyHandler(ctx.channel()));
                        }

                    });

                } else {

                    ctx.writeAndFlush(new DefaultSocks5CommandResponse(
                            Socks5CommandStatus.FAILURE,
                            request.dstAddrType())
                    );

                    $.closeOnFlush(ctx.channel());

                }

            }

        });

        if (socksServerConfig.isDispatchUseSocksv5()) {
            socksV5Dispatch(ctx, promise, request);
        } else {
            normalDispatch(ctx, promise, request);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("handle socks command CONNECT fail!", cause);
        $.closeOnFlush(ctx.channel());
    }
}

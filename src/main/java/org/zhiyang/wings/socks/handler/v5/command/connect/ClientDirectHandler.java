package org.zhiyang.wings.socks.handler.v5.command.connect;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lizhiyang
 */
public class ClientDirectHandler extends ChannelInboundHandlerAdapter {

    private final Logger log = LoggerFactory.getLogger(ClientDirectHandler.class);

    private final Promise<Channel> promise;

    public ClientDirectHandler(Promise<Channel> promise) {
        this.promise = promise;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.pipeline().remove(this);
        this.promise.setSuccess(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.pipeline().remove(this);
        this.promise.setFailure(cause);
        ctx.fireExceptionCaught(cause);
    }
}

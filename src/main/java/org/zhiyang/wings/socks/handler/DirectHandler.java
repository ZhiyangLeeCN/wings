package org.zhiyang.wings.socks.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Promise;

/**
 * @author lizhiyang.
 */
public class DirectHandler extends ChannelInboundHandlerAdapter {

    private final Promise<Channel> promise;

    public DirectHandler(Promise<Channel> promise) {
        this.promise = promise;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        promise.setSuccess(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        promise.setFailure(cause);
    }
}

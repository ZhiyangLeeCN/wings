package org.zhiyang.wings.socks.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.zhiyang.wings.$;

/**
 * @author lizhiyang.
 */
public class ReplyHandler extends ChannelInboundHandlerAdapter {

    private final Channel replyChannel;

    public ReplyHandler(Channel replyChannel) {
        this.replyChannel = replyChannel;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (replyChannel.isActive()) {
            replyChannel.writeAndFlush(msg);
        } else {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (replyChannel.isActive()) {
            $.closeOnFlush(replyChannel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (replyChannel.isActive()) {
            $.closeOnFlush(replyChannel);
        }
    }
}

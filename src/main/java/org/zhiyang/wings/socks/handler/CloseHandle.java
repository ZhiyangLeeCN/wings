package org.zhiyang.wings.socks.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhiyang.wings.$;

/**
 * @author lizhiyang
 */
public class CloseHandle extends ChannelInboundHandlerAdapter {

    private final Logger log = LoggerFactory.getLogger(CloseHandle.class);

    private final Channel relatedChannel;

    public CloseHandle(@Nullable Channel relatedChannel) {
        this.relatedChannel = relatedChannel;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (relatedChannel != null && relatedChannel.isActive()) {
            $.closeOnFlush(relatedChannel);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("close connection because error occurred, ctx name:" + ctx.name(), cause);

        if (relatedChannel != null) {
            if (relatedChannel.isActive()) {
                $.closeOnFlush(relatedChannel);
            }
        }
        $.closeOnFlush(ctx.channel());
    }
}

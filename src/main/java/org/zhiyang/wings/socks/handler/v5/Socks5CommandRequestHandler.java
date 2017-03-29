package org.zhiyang.wings.socks.handler.v5;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequest;
import io.netty.handler.codec.socksx.v5.Socks5CommandStatus;
import io.netty.handler.codec.socksx.v5.Socks5CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhiyang.wings.$;
import org.zhiyang.wings.socks.SocksServerConfig;
import org.zhiyang.wings.socks.handler.v5.command.connect.Socks5ConnectHandler;

/**
 * @author lizhiyang.
 */
public class Socks5CommandRequestHandler extends SimpleChannelInboundHandler<Socks5CommandRequest> {

    private final Logger log = LoggerFactory.getLogger(Socks5CommandRequestHandler.class);

    private final SocksServerConfig socksServerConfig;

    public Socks5CommandRequestHandler(SocksServerConfig socksServerConfig) {
        this.socksServerConfig = socksServerConfig;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Socks5CommandRequest msg) throws Exception {

        final Socks5CommandRequest request = msg;
        if (request.type().equals(Socks5CommandType.CONNECT)) {

            ChannelPipeline pipeline = ctx.pipeline();
            pipeline.addLast(new Socks5ConnectHandler(this.socksServerConfig));
            pipeline.remove(this);
            ctx.fireChannelRead(msg);

        } else {

            //other command not support
            ctx.writeAndFlush(new DefaultSocks5CommandResponse(
                    Socks5CommandStatus.FAILURE,
                    request.dstAddrType())
            );

            $.closeOnFlush(ctx.channel());

        }

    }

}

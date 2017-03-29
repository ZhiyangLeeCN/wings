package org.zhiyang.wings.socks.handler.v5;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialResponse;
import io.netty.handler.codec.socksx.v5.Socks5AuthMethod;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhiyang.wings.socks.SocksServerConfig;

/**
 * @author lizhiyang.
 */
public class Socks5InitialRequestHandler extends SimpleChannelInboundHandler<Socks5InitialRequest> {

    private final Logger log = LoggerFactory.getLogger(Socks5InitialRequestHandler.class);

    private final SocksServerConfig socksServerConfig;

    public Socks5InitialRequestHandler(SocksServerConfig socksServerConfig) {
        this.socksServerConfig = socksServerConfig;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Socks5InitialRequest msg) throws Exception {

        final Socks5InitialRequest request = msg;

        if (request.authMethods().contains(Socks5AuthMethod.NO_AUTH)) {

            ctx.pipeline().remove(this);
            ctx.writeAndFlush(new DefaultSocks5InitialResponse(Socks5AuthMethod.NO_AUTH));

        } else {

            ctx.writeAndFlush(new DefaultSocks5InitialResponse(Socks5AuthMethod.UNACCEPTED));
            ctx.close();

        }

    }
}

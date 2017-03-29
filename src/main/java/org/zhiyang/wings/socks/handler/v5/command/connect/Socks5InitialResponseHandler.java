package org.zhiyang.wings.socks.handler.v5.command.connect;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.Socks5AuthMethod;
import io.netty.handler.codec.socksx.v5.Socks5InitialResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhiyang.wings.socks.SocksServerConfig;

/**
 * @author lizhiyang
 */
public class Socks5InitialResponseHandler extends SimpleChannelInboundHandler<Socks5InitialResponse> {

    private final Logger log = LoggerFactory.getLogger(Socks5InitialResponseHandler.class);

    private final SocksServerConfig socksServerConfig;

    public Socks5InitialResponseHandler(SocksServerConfig socksServerConfig) {
        this.socksServerConfig = socksServerConfig;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Socks5InitialResponse msg) throws Exception {

        if (msg.authMethod() != Socks5AuthMethod.UNACCEPTED) {

            ctx.pipeline().remove(this);
            ctx.fireChannelRead(msg);

        } else {

            ctx.close();

        }

    }

}

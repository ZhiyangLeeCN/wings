package org.zhiyang.wings.socks.handler.v5.command.connect;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socksx.v5.Socks5CommandResponse;
import io.netty.handler.codec.socksx.v5.Socks5CommandStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhiyang.wings.socks.SocksServerConfig;

/**
 * @author lizhiyang
 */
public class Socks5CommandResponseHandler extends SimpleChannelInboundHandler<Socks5CommandResponse> {

    private final Logger log = LoggerFactory.getLogger(Socks5CommandResponseHandler.class);

    private final SocksServerConfig socksServerConfig;

    public Socks5CommandResponseHandler(SocksServerConfig socksServerConfig) {
        this.socksServerConfig = socksServerConfig;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Socks5CommandResponse msg) throws Exception {

        if (msg.status() == Socks5CommandStatus.SUCCESS) {

            ctx.pipeline().remove(this);
            ctx.fireChannelRead(msg);

        } else {

            log.error("socks server response error:{}", msg.status().toString());
            ctx.close();

        }

    }

}

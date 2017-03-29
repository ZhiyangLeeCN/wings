package org.zhiyang.wings.socks.handler.v5.command.connect;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialRequest;
import io.netty.handler.codec.socksx.v5.Socks5AuthMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhiyang.wings.socks.SocksServerConfig;

/**
 * @author lizhiyang
 */
public class SendSocks5InitialRequest extends ChannelInboundHandlerAdapter {

    private final Logger log = LoggerFactory.getLogger(SendSocks5InitialRequest.class);

    private final SocksServerConfig socksServerConfig;

    public SendSocks5InitialRequest(SocksServerConfig socksServerConfig) {
        this.socksServerConfig = socksServerConfig;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new DefaultSocks5InitialRequest(
                Socks5AuthMethod.NO_AUTH)
        );
    }
}

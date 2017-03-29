package org.zhiyang.wings.socks.handler.v5.command.connect;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequest;
import io.netty.handler.codec.socksx.v5.Socks5CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhiyang.wings.socks.SocksServerConfig;

/**
 * @author lizhiyang
 */
public class SendSocks5ConnectCommand extends ChannelInboundHandlerAdapter {

    private final Logger log = LoggerFactory.getLogger(SendSocks5ConnectCommand.class);

    private final SocksServerConfig socksServerConfig;

    private final Socks5CommandRequest request;

    public SendSocks5ConnectCommand(SocksServerConfig socksServerConfig, Socks5CommandRequest request) {
        this.socksServerConfig = socksServerConfig;
        this.request = request;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        final Socks5CommandRequest request = new DefaultSocks5CommandRequest(
                Socks5CommandType.CONNECT,
                SendSocks5ConnectCommand.this.request.dstAddrType(),
                SendSocks5ConnectCommand.this.request.dstAddr(),
                SendSocks5ConnectCommand.this.request.dstPort()
        );

        ctx.writeAndFlush(request);
        ctx.pipeline().remove(this);

    }
}

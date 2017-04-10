package org.zhiyang.wings.socks.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhiyang.wings.Log;

import java.net.InetSocketAddress;

/**
 * @author lizhiyang
 */
public class TrafficLogHandler extends ChannelTrafficShapingHandler {

    private final Logger log = LoggerFactory.getLogger(Log.TRAFFIC_INFO);
    private long startTime = 0;

    public TrafficLogHandler(long checkInterval) {
        super(checkInterval);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        startTime = System.currentTimeMillis();
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        long endTime = System.currentTimeMillis();
        long timeSpent = endTime - startTime;
        InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        long writeByte = this.trafficCounter().cumulativeWrittenBytes();
        long readByte = this.trafficCounter().cumulativeReadBytes();

        log.info("[{}] {} {}:{} to {}:{} {} {} {}",
                Log.TRAFFIC_INFO,
                timeSpent,
                localAddress.getAddress().getHostAddress(),
                localAddress.getPort(),
                remoteAddress.getAddress().getHostAddress(),
                remoteAddress.getPort(),
                writeByte,
                readByte,
                writeByte + readByte
        );

        super.channelInactive(ctx);
    }
}

package com.example.reconnection.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 2:03
 * @Version 1.0
 * 处理类
 */
@Slf4j
public class ReconnectionServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("server channelRead..");
        log.info(ctx.channel().remoteAddress() + "->Server :" + msg.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("出现异常" + ctx.channel().config());
        ctx.close();
    }
}

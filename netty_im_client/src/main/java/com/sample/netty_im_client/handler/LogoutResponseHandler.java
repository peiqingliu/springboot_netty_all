package com.sample.netty_im_client.handler;

import com.example.netty_common.protocol.response.LogoutResponsePacket;
import com.example.netty_common.util.SessionUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 12:15
 * @Version 1.0
 */
public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket logoutResponsePacket) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
    }
}

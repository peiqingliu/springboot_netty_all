package com.example.netty_reconnection_client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 3:11
 * @Version 1.0
 * 客户端处理类
 */
@Slf4j
@ChannelHandler.Sharable
public class ReconnectClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("ReconnectClientHandler channelActive");
        log.info("连接成功，当前时间为" + LocalDate.now());
        ctx.fireChannelActive();
    }

    /**
     * 断开连接触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("ReconnectClientHandler channelInactive");
        log.info("断开连接时间"+LocalDate.now());
    }

    /**
     * 读取服务端信息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;
        log.info("服务端反悔的信息为：" + message);
        if (message.equals("Heartbeat")) {
            ctx.write("has read message from server");
            ctx.flush();
        }
        ReferenceCountUtil.release(msg);
    }
}

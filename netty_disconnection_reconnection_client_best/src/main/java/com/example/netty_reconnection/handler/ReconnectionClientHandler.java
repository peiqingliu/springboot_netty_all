package com.example.netty_reconnection.handler;

import com.example.netty_reconnection.client.ReconnectClient;
import com.example.netty_reconnection.ware.Middleware;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 14:40
 * @Version 1.0
 */
@Slf4j
public class ReconnectionClientHandler extends Middleware {

    private volatile boolean reconnect = true;

    /**
     * 重连尝试次数 最多12次，之后还不能重连上，则剔除客户端
     */
    private int attempts;


    private ReconnectClient client;

    public ReconnectionClientHandler(ReconnectClient client){
        super("client");
        this.client = client;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       log.info("client  收到数据： " + msg.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("当前链路已经激活了，重连尝试次数重新置为0");
        attempts = 0;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        log.info("此时连接为断开状态");
        if (reconnect){
            log.info("链接断开，尝试重连");
            if (attempts < 12){
                attempts++;
                log.info("重连次数attempts:" + attempts);
                client.doConnect();
            }
        }
        ctx.fireChannelActive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.error(name + "exception :"+ cause.toString());
    }

}

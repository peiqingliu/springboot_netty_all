package com.example.netty_heartBeat.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 前面简单地了解了一下IdleStateHandler，我们现在写一个简单的心跳demo：
 *
 * 1）服务器端每隔5秒检测服务器端的读超时，如果5秒没有接受到客户端的写请求，也就说服务器端5秒没有收到读事件，则视为一次超时
 *
 * 2）如果超时二次则说明连接处于不活跃的状态，关闭ServerChannel
 *
 * 3）客户端每隔4秒发送一些写请求，这个请求相当于一次心跳包，告之服务器端：客户端仍旧活着
 */

/**
 * 服务端的handler,集成ChannelHandlerAdapter，我们先重写userEventTriggered方法，
 * 该方法在超时的时候会自动触发相应的超时事件
 */
@Slf4j
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {

    private int loss_connect_time = 0;

    /**
     * 读
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("server chanel read");
        log.info(ctx.channel().remoteAddress() + "msg:" + msg.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("出现异常");
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //属于超时事件
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE){
                //读超时
                loss_connect_time++;
                log.info("5秒没有接收到客户端的信息了");
                if (loss_connect_time > 12){
                    log.info("超过两次，关闭这个不活跃的连接");
                    ctx.channel().close();
                }
            }
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

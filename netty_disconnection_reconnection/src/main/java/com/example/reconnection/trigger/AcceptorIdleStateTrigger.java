package com.example.reconnection.trigger;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 1:50
 * @Version 1.0
 * 单独写一个AcceptorIdleStateTrigger，其实也是继承ChannelInboundHandlerAdapter，
 * 重写userEventTriggered方法，
 * 因为客户端是write，那么服务端自然是read，设置的状态就是IdleState.READER_IDLE
 */
@Slf4j
@ChannelHandler.Sharable
public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter {

    private int loss_connect_time = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent){
//            IdleStateEvent event = (IdleStateEvent) evt;
//            if (event.state() == IdleState.READER_IDLE){
//                //读超时
//                log.info("从客户端，读取数据超时");
//                throw new Exception("idle exception");
//            }
//        }else {
//            super.userEventTriggered(ctx,evt);
//        }

        //属于超时事件
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE){
                //读超时
                loss_connect_time++;
                log.info("10秒没有接收到客户端的信息了");
                if (loss_connect_time > 10){
                    log.info("超过10次，关闭这个不活跃的连接:" + ctx.channel().config());
                    ctx.channel().close();
                }
            }
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

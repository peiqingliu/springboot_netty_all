package com.example.netty_reconnection_client.watch;


import com.example.netty_reconnection_client.holder.ChannelHandlerHolder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 2:10
 * @Version 1.0
 * 重点
 * 链路检测狗
 * 重连检测狗，当发现当前的链路不稳定关闭之后，进行12次重连
 *
 * 1)继承了ChannelInboundHandlerAdapter，说明它也是Handler，也对，作为一个检测对象，肯定会放在链路中，否则怎么检测.
 *
 * 2）实现了2个接口，TimeTask，ChannelHandlerHolder
 *
 *    ①TimeTask，我们就要写run方法，这应该是一个定时任务，这个定时任务做的事情应该是重连的工作
 *
 *    ②ChannelHandlerHolder的接口，这个接口我们刚才说过是维护的所有的Handlers，因为在重连的时候需要获取Handlers
 *
 * 3）bootstrap对象，重连的时候依旧需要这个对象
 *
 * 4）当链路断开的时候会触发channelInactive这个方法，也就说触发重连的导火索是从这边开始的
 */

@Slf4j
@Sharable
public abstract class ConnectionWatchdog extends ChannelInboundHandlerAdapter implements TimerTask , ChannelHandlerHolder {

    //bootstrap对象，重连的时候依旧需要这个对象
    private final Bootstrap bootstrap;
    private final Timer timer;
    private final int port;
    private final String host;

    private volatile boolean reconnect = true;

    /**
     * 重连尝试次数 最多12次，之后还不能重连上，则剔除客户端
     */
    private int attempts;

    protected ConnectionWatchdog(Bootstrap bootstrap, Timer timer, int port, String host, boolean b) {
        this.bootstrap = bootstrap;
        this.timer = timer;
        this.port = port;
        this.host = host;
    }

    /**
     * 连接成功
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("当前链路已经激活了，重连尝试次数重新置为0");
        attempts = 0;
        ctx.fireChannelActive();
    }

    /**
     * 断开连接
     * @param ctx
     * @throws Exception
     * 触发重连的导火索
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("此时连接为断开状态");
        if (reconnect){
            log.info("链接断开，尝试重连");
            if (attempts < 12){
                attempts++;
                //重连的间隔时间越来越长
                int timeOut = 2 << attempts;
                /**
                 * 触发定时器任务中的方法
                 */
                timer.newTimeout(this,timeOut, TimeUnit.MILLISECONDS);
            }
        }
        ctx.fireChannelActive();
    }

    /**
     * 定时器方法
     * @param timeout
     * @throws Exception
     */
    @Override
    public void run(Timeout timeout) throws Exception {
        log.info("重新连接开始");
        ChannelFuture future;
        //此处需要同步
        synchronized(bootstrap){
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                    //添加handlers
                    ch.pipeline().addLast(handlers());
                }
            });
            future = bootstrap.connect(host,port).sync();
        }

        //添加监听器
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {

                //如果重连一直失败，就一直重连，直到12次
                // //如果重连失败，则调用ChannelInactive方法，再次出发重连事件，一直尝试12次，如果失败则不再重连
                if (!f.isSuccess()){
                    log.info("重连失败");
                    f.channel().pipeline().fireChannelInactive();
                }else {
                    log.info("重连成功");
                }
            }
        });

    }
}

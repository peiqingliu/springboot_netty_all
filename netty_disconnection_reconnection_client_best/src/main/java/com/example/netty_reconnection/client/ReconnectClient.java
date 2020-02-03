package com.example.netty_reconnection.client;

import com.example.netty_reconnection.handler.ReconnectionClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 14:44
 * @Version 1.0
 */
@Slf4j
public class ReconnectClient {

    private NioEventLoopGroup worker = new NioEventLoopGroup();

    private Channel channel;

    private Bootstrap bootstrap;

    //客户端连接开始
    public void start(){
        bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        /**
                         * 设置的写超时间要小于 服务端读的超时时间
                         */
                        pipeline.addLast(new IdleStateHandler(0,9,0));
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());

                        pipeline.addLast(new ReconnectionClientHandler(ReconnectClient.this));
                    }
                });
        doConnect();
    }

    public void doConnect(){
        if (channel != null && channel.isActive()){
            return;
        }
        ChannelFuture cf = bootstrap.connect("127.0.0.1",9000);
        //实现监听通道的连接方法
        cf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()){
                    channel = channelFuture.channel();
                    log.info("连接成功");
                }else {
                    log.info("每隔10秒连接一次");
                    channelFuture.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    },10, TimeUnit.SECONDS);
                }
            }
        });

    }

}

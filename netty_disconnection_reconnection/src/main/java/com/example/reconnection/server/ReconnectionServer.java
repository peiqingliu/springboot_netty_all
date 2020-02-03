package com.example.reconnection.server;

import com.example.reconnection.handler.ReconnectionServerHandler;
import com.example.reconnection.trigger.AcceptorIdleStateTrigger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 1:47
 * @Version 1.0
 */
@Slf4j
public class ReconnectionServer {

    private final AcceptorIdleStateTrigger idleStateTrigger = new AcceptorIdleStateTrigger();

    private int port;

    public ReconnectionServer(int port) {
        this.port = port;
    }

    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    //开启服务
    public void start(){

        ServerBootstrap sb = new ServerBootstrap();
        sb.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024) //  tcp最大缓存链接个数 配置TCP参数，将其中一个参数backlog设置为1024，表明临时存放已完成三次握手的请求的队列的最大长度。
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        /**
                         * 心跳检测包
                         * 在服务器端会每隔10秒来检查一下channelRead方法被调用的情况，
                         * 如果在5秒内该链上的channelRead方法都没有被触发，就会调用userEventTriggered方法：
                         */
                        ch.pipeline().addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
                        /**
                         *
                         */
                        ch.pipeline().addLast(idleStateTrigger);
                        ch.pipeline().addLast("decoder", new StringDecoder());
                        ch.pipeline().addLast("encoder", new StringEncoder());
                        ch.pipeline().addLast(new ReconnectionServerHandler());
                    }
                });
        try {
            ChannelFuture cf = sb.bind(port).sync();
            if (cf.isSuccess()){
                log.info("netty服务端启动成功。");
            }
        } catch (InterruptedException e) {
            log.error("netty服务端启动出现异常");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}

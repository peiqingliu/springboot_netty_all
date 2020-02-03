package com.example.netty_reconnection_client.client;

import com.example.netty_reconnection_client.handler.ReconnectClientHandler;
import com.example.netty_reconnection_client.trigger.ConnectorIdleStateTrigger;
import com.example.netty_reconnection_client.watch.ConnectionWatchdog;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 3:07
 * @Version 1.0
 */
@Slf4j
public class ReconnectClient {

    protected final HashedWheelTimer timer = new HashedWheelTimer();

    private Bootstrap bootstrap;

    private final ConnectorIdleStateTrigger idleStateTrigger = new ConnectorIdleStateTrigger();

    //连接
    public void connect(int port, String host){
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .handler(new LoggingHandler(LogLevel.INFO));

        final ConnectionWatchdog watchdog = new ConnectionWatchdog(bootstrap, timer, port,host, true) {
            @Override
            public ChannelHandler[] handlers() {
                return new ChannelHandler[]{
                        this,
                        new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS),
                        idleStateTrigger,
                        new StringDecoder(),
                        new StringEncoder(),
                        new ReconnectClientHandler()

                };
            }
        };

        ChannelFuture cf;
        //进行连接
        try {
            synchronized (bootstrap){
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(watchdog.handlers());
                    }
                });
                cf = bootstrap.connect(host,port);
            }
            // 以下代码在synchronized同步块外面是安全的
            cf.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("链接失败");
        }
    }

}

package com.example.netty_reconnection_client.client;

import com.example.netty_reconnection_client.handler.HeartBeatClientHandler;
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
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 心跳机制客户端
 */
@Component
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class HeartBeatsClient {

    // 要请求的服务器的ip地址
    private String ip = "127.0.0.1";
    // 服务器的端口
    private int port = 9000;

    EventLoopGroup bossGroup = new NioEventLoopGroup();

    public HeartBeatsClient(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    /**
     * 客户端进行连接
     */
    @PostConstruct
    public void connect(){

        try {
            Bootstrap bs = new Bootstrap();
            bs.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline cp = ch.pipeline();
                            /**
                             * 客户端代码也要加入IdleStateHandler这个handler，注意的是，我们要注意的是写超时，所以要设置写超时的时间，
                             * 因为服务器端是5秒检测读超时，所以客户端必须在5秒内发送一次心跳，告之服务端，所以我们设置4秒：
                             * 客户端每隔4秒发送一些写请求，这个请求相当于一次心跳包，告之服务器端：客户端仍旧活着
                             */
                            cp.addLast("ping",new IdleStateHandler(0,4,0, TimeUnit.SECONDS));
                            cp.addLast("decoder", new StringDecoder());
                            cp.addLast("encoder", new StringEncoder());
                            cp.addLast(new HeartBeatClientHandler());
                        }
                    });


            ChannelFuture cf = bs.connect(ip, port).sync();
            if (cf.isSuccess()){
                log.info("连接成功。");
            }
            cf.channel().closeFuture().sync();

        }catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}

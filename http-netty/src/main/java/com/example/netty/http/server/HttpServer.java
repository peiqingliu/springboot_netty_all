package com.example.netty.http.server;

import com.example.netty.http.handler.HttpServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
@AllArgsConstructor
@NoArgsConstructor
public class HttpServer {

    private int port = 8888;

    public HttpServer(int port) {
        this.port = port;
    }

    //创建两个线程组
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workGroup = new NioEventLoopGroup(20);

    //服务端启动
    @PostConstruct
    public void start(){
        try {
            //Nio启动类
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler( new HttpServerChannelInitializer());
            ChannelFuture channelFuture = sb.bind(port).sync();
            if (channelFuture.isSuccess()){
                log.info("服务启动成功");
            }
        }catch (Exception e){

        }


    }

    //优雅退出
    @PreDestroy
    private void destroy(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
        log.info("server关闭");
    }
}

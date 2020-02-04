package com.example.communication.server;

import com.example.communication.handler.FirstServerHandler;
import com.example.communication.handler.LoginRequestHandler;
import com.example.communication.handler.MessageRequestHandler;
import com.example.communication.handler.ServerHandler;
import com.example.netty_common.codec.PacketDecoder;
import com.example.netty_common.codec.PacketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 15:44
 * @Version 1.0
 */
@Slf4j
@Component
@NoArgsConstructor
public class CommunicationServer {

    private static final int port = 9000;
    NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    NioEventLoopGroup workGroup = new NioEventLoopGroup();

    @PostConstruct
    public void start(){
        final ServerBootstrap sb = new ServerBootstrap();
        sb.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //ch.pipeline().addLast(new FirstServerHandler());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginRequestHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                        //ch.pipeline().addLast(new ServerHandler());
                    }
                });
        sb.bind(port).addListener(future -> {
            if (future.isSuccess()){
                log.info("启动成功。");
            }
        });
    }

    //优雅退出
    @PreDestroy
    private void destroy(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
        log.info("server关闭");
    }

}

package com.example.netty_im.server;

import com.example.netty_im.codec.PacketCodecHandler;
import com.example.netty_im.codec.Spliter;
import com.example.netty_im.handler.IMIdleStateHandler;
import com.example.netty_im.server.handler.AuthHandler;
import com.example.netty_im.server.handler.HeartBeatRequestHandler;
import com.example.netty_im.server.handler.IMHandler;
import com.example.netty_im.server.handler.LoginRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 20:54
 * @Version 1.0
 */
@Slf4j
@Component
@NoArgsConstructor
@AllArgsConstructor
public class NettyServer {

    private static final int PORT = 9000;

    NioEventLoopGroup boosGroup = new NioEventLoopGroup();
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    final ServerBootstrap sb = new ServerBootstrap();

    @PostConstruct
    public void start(){
        sb.group(boosGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 空闲检测 心跳
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(PacketCodecHandler.INSTANCE);
                        ch.pipeline().addLast(LoginRequestHandler.INSTANCE);
                        ch.pipeline().addLast(HeartBeatRequestHandler.INSTANCE);
                        ch.pipeline().addLast(AuthHandler.INSTANCE);
                        ch.pipeline().addLast(IMHandler.INSTANCE);
                    }
                });
        sb.bind(PORT).addListener(future -> {
            if (future.isSuccess()){
                log.info("server启动成功。");
            }else {
                log.info("server启动失败。");
            }
        });
    }


    //优雅退出
    @PreDestroy
    private void destroy(){
        boosGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.info("server关闭");
    }
}

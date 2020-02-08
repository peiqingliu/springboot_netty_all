package com.exanple.package_server.server;

import com.example.netty.code.code.PacketDecoder;
import com.example.netty.code.code.PacketDecoderUS;
import com.example.netty.code.code.PacketEncoder;
import com.example.netty.code.code.PacketEncoderUS;
import com.exanple.package_server.handler.LoginRequestHandler;
import com.exanple.package_server.handler.PackageHandler;
import com.exanple.package_server.handler.RegisterRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Author Liupeiqing
 * @Date 2020/2/5 12:20
 * @Version 1.0
 */
@Slf4j
@Component
@NoArgsConstructor
public class PackageServer {

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
                        //ch.pipeline().addLast(new PackageHandler());

//                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
//                        //1.先添加 解码器
//                        ch.pipeline().addLast(new PacketDecoder());
//                        ch.pipeline().addLast(new LoginRequestHandler());
//
//                        //ch.pipeline().addLast(new MessageRequestHandler());
//
//                        //最后添加编码器
//                        ch.pipeline().addLast(new PacketEncoder());
                        //1.添加分割器
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 4, 1,2,0));
                        //2.先添加 解码器
                        ch.pipeline().addLast(new PacketDecoderUS());
                        ch.pipeline().addLast(new RegisterRequestHandler());
                        ch.pipeline().addLast(new PacketEncoderUS());
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

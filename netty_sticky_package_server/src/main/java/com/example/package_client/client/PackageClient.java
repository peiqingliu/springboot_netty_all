package com.example.package_client.client;

import com.example.netty.code.code.PacketDecoder;
import com.example.netty.code.code.PacketDecoderUS;
import com.example.netty.code.code.PacketEncoder;
import com.example.netty.code.code.PacketEncoderUS;
import com.example.package_client.handler.LoginResponseHandler;
import com.example.package_client.handler.PackageHandler;
import com.example.package_client.handler.RegisterResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

/**
 * @Author Liupeiqing
 * @Date 2020/2/5 12:27
 * @Version 1.0
 */
@Slf4j
public class PackageClient {

    private static final int MAX_RETRY = 5;
    private static final int port = 9000;
    private static final String host = "127.0.0.1";

    public static void start(){
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bs = new Bootstrap();

        bs
                // 1.指定线程模型
                .group(workerGroup)
                // 2.指定 IO 类型为 NIO
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //ch.pipeline().addLast(new PackageHandler());
//                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
//                        ch.pipeline().addLast(new PacketDecoder());
//                        ch.pipeline().addLast(new LoginResponseHandler());
//                        ch.pipeline().addLast(new PacketEncoder());

                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 4, 1,2,0));
                        ch.pipeline().addLast(new PacketDecoderUS());
                        ch.pipeline().addLast(new RegisterResponseHandler());
                        ch.pipeline().addLast(new PacketEncoderUS());

                    }
                });

        connect(bs,host,port,MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
                // 连接成功之后，启动控制台线程
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(LocalDate.now() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });
    }


}

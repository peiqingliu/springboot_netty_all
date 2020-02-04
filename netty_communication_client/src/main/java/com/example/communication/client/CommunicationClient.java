package com.example.communication.client;

import com.example.communication.handler.ClientHandler;
import com.example.communication.handler.FirstClientHandler;
import com.example.communication.handler.LoginResponseHandler;
import com.example.communication.handler.MessageResponseHandler;
import com.example.communication.util.LoginUtil;
import com.example.netty_common.codec.PacketDecoder;
import com.example.netty_common.codec.PacketEncoder;
import com.example.netty_common.protocol.PacketCodec;
import com.example.netty_common.protocol.request.MessageRequestPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 15:55
 * @Version 1.0
 */
public class CommunicationClient {

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
                        //ch.pipeline().addLast(new FirstClientHandler());
                        //ch.pipeline().addLast(new ClientHandler());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginResponseHandler());
                        ch.pipeline().addLast(new MessageResponseHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });

        connect(bs,host,port,MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
                // 连接成功之后，启动控制台线程
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
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

    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (LoginUtil.hasLogin(channel)) {
                    System.out.println("输入消息发送至服务端: ");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();

                    MessageRequestPacket packet = new MessageRequestPacket();
                    packet.setMessage(line);
                    ByteBuf byteBuf = channel.alloc().buffer();
                    PacketCodec.INSTANCE.encode(byteBuf, packet);
                    channel.writeAndFlush(byteBuf);
                }
            }
        }).start();
    }
}

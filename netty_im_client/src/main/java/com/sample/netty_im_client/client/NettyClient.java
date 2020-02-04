package com.sample.netty_im_client.client;

import com.example.netty_common.codec.PacketDecoder;
import com.example.netty_common.codec.PacketEncoder;
import com.example.netty_common.codec.Spliter;
import com.example.netty_common.util.SessionUtil;
import com.sample.netty_im_client.console.ConsoleCommandManager;
import com.sample.netty_im_client.console.LoginConsoleCommand;
import com.sample.netty_im_client.handler.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 11:26
 * @Version 1.0
 */
@Slf4j
public class NettyClient {

    private static final int MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 9000;

    private static  EventLoopGroup workGroup = new NioEventLoopGroup();

    private static  Bootstrap bootstrap = new Bootstrap();

    public static void start(){
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 空闲检测
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        ch.pipeline().addLast(new Spliter());

                        //解码器
                        ch.pipeline().addLast(new PacketDecoder());

                        // 登录响应处理器
                        ch.pipeline().addLast(new LoginResponseHandler());
                        // 收消息处理器
                        ch.pipeline().addLast(new MessageResponseHandler());
                        // 创建群响应处理器
                        ch.pipeline().addLast(new CreateGroupResponseHandler());
                        // 加群响应处理器
                        ch.pipeline().addLast(new JoinGroupResponseHandler());
                        // 退群响应处理器
                        ch.pipeline().addLast(new QuitGroupResponseHandler());
                        // 获取群成员响应处理器
                        ch.pipeline().addLast(new ListGroupMembersResponseHandler());
                        // 群消息响应
                        ch.pipeline().addLast(new GroupMessageResponseHandler());
                        // 登出响应处理器
                        ch.pipeline().addLast(new LogoutResponseHandler());

                        //编码器
                        ch.pipeline().addLast(new PacketEncoder());

                        // 心跳定时器
                        ch.pipeline().addLast(new HeartBeatTimerHandler());
                    }
                });
        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap,String host,int port,int retry){
        bootstrap.connect(host,port).addListener(future -> {
            if (future.isSuccess()){
                log.info("连接成功，启动控制台线程……" + LocalDate.now());
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            }else if (retry == 0){
                log.error("重连次数已用完，放弃连接");
            }else {
                int order = (MAX_RETRY - retry) + 1;
                log.error(LocalDate.now() + ": 连接失败，第" + order + "次重连……");
                // 本次重连的间隔
                int delay = 1 << order;
                bootstrap.config().group()
                        .schedule(() -> connect(bootstrap,host,port,retry-1),delay, TimeUnit.SECONDS);

            }
        });
    }

    private static void startConsoleThread(Channel channel){
        ConsoleCommandManager manager = new ConsoleCommandManager();
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();

        Scanner scanner = new Scanner(System.in);

        new Thread( () -> {
            while (!Thread.interrupted()){
                if (SessionUtil.hasLogin(channel)){
                    manager.exec(scanner,channel);
                }else {
                    loginConsoleCommand.exec(scanner,channel);
                }
            }
        }).start();
    }

}

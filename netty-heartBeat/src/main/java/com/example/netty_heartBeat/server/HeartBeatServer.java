package com.example.netty_heartBeat.server;

import com.example.netty_heartBeat.handler.HeartBeatServerHandler;
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
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * 心跳服务端
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Component
public class HeartBeatServer {


    private int port = 9000;

    //一个主线程组
    EventLoopGroup bossGroup  = new NioEventLoopGroup(1);

    //2 第二个线程组 是用于实际的业务处理操作的
    // 用来处理已经被接收的连接，一旦bossGroup接收到连接，就会把连接信息注册到workerGroup上
    EventLoopGroup workGroup = new NioEventLoopGroup(20);
    //服务端启动

    /**
     * 实例化 之后执行
     */
    @PostConstruct
    public void start() {
        //启动器
        ServerBootstrap sb = new ServerBootstrap();
        sb.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)  //我要指定使用NioServerSocketChannel这种类型的通道
                .option(ChannelOption.SO_BACKLOG,1024) //  tcp最大缓存链接个数 配置TCP参数，将其中一个参数backlog设置为1024，表明临时存放已完成三次握手的请求的队列的最大长度。
                .childOption(ChannelOption.SO_KEEPALIVE,true) //设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文。
                .handler(new LoggingHandler(LogLevel.INFO))  //打印日志级别
                //一定要使用 childHandler 去绑定具体的 事件处理器
                //用于处理客户端的IO事件，比如有一个客户端发起请求，要读取数据，就可以使用这里面的类来处理这个事件。
                // 这是整个处理的核心。也是我们自己主要关注的类。
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        /**
                         * 此处设置了心跳检测机制  每隔5秒 就进行一次读检测
                         */
                        ch.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                        ch.pipeline().addLast("decoder", new StringDecoder());
                        ch.pipeline().addLast("encoder", new StringEncoder());
                        ch.pipeline().addLast(new HeartBeatServerHandler());
                    }
                });

        try {

            ChannelFuture cf = sb.bind(port).sync();
            if (cf.isSuccess()){
                log.info("server开启成功。");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
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

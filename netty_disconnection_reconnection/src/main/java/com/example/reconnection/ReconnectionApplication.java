package com.example.reconnection;


import com.example.reconnection.server.ReconnectionServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 下Netty应用心跳和重连的整个过程：
 *
 * 1）客户端连接服务端
 *
 * 2）在客户端的的ChannelPipeline中加入一个比较特殊的IdleStateHandler，设置一下客户端的写空闲时间，例如5s
 *
 * 3）当客户端的所有ChannelHandler中4s内没有write事件，则会触发userEventTriggered方法（上文介绍过）
 *
 * 4）我们在客户端的userEventTriggered中对应的触发事件下发送一个心跳包给服务端，检测服务端是否还存活，防止服务端已经宕机，
 *  客户端还不知道
 *
 * 5）同样，服务端要对心跳包做出响应，其实给客户端最好的回复就是“不回复”，这样可以服务端的压力，
 *  假如有10w个空闲Idle的连接，那么服务端光发送心跳回复，则也是费事的事情，那么怎么才能告诉客户端它还活着呢，
 *  其实很简单，因为5s服务端都会收到来自客户端的心跳信息，那么如果10秒内收不到，服务端可以认为客户端挂了，可以close链路
 *
 * 6）加入服务端因为什么因素导致宕机的话，就会关闭所有的链路链接，所以作为客户端要做的事情就是短线重连

 *
 *
 *
 * 要写工业级的Netty心跳重连的代码，需要解决一下几个问题：
 *
 * 1）ChannelPipeline中的ChannelHandlers的维护，首次连接和重连都需要对ChannelHandlers进行管理
 *
 * 2）重连对象的管理，也就是bootstrap对象的管理
 *
 * 3）重连机制编写
 *
 *
 */

/**
 * netty断线重连
 */
@SpringBootApplication
public class ReconnectionApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReconnectionApplication.class,args);
        //启动服务
        new ReconnectionServer(9000).start();
    }
}

package com.example.package_client.handler;

import com.example.netty.code.request.LoginRequestPacket;
import com.example.netty.code.response.LoginResponsePacket;
import com.example.netty.code.util.LoginUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.UUID;

/**
 * @Author Liupeiqing
 * @Date 2020/2/6 23:25
 * @Version 1.0
 * 客户端响应代码
 *
 * 基于 SimpleChannelInboundHandler，我们可以实现每一种指令的处理，不再需要强转，不再有冗长乏味的 if else 逻辑，不需要手动传递对象。
 */
@Slf4j
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    /**
     * 连接成功
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for (int i = 0;i<100;i++){
            // 创建登录对象
            LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
            loginRequestPacket.setUserId(UUID.randomUUID().toString());
            loginRequestPacket.setUserName("liupeiqing");
            loginRequestPacket.setPwd("pwd");

            // 写数据
            ctx.channel().writeAndFlush(loginRequestPacket);
        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) throws Exception {
        log.info("获取响应");
        if (loginResponsePacket.isSuccess()) {
            log.info(new Date() + ": 客户端登录成功");
            LoginUtil.markAsLogin(ctx.channel());
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
        }
    }
}

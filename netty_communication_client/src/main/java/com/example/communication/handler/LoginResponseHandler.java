package com.example.communication.handler;

import com.example.communication.util.LoginUtil;
import com.example.netty_common.protocol.request.LoginRequestPacket;
import com.example.netty_common.protocol.response.LoginResponsePacket;
import com.example.netty_common.protocol.response.LogoutResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.UUID;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 18:57
 * @Version 1.0
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket > {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserName("liupeiqing");
        loginRequestPacket.setPassword("123456");

        // 写数据
        ctx.channel().writeAndFlush(loginRequestPacket);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {
        if (loginResponsePacket.isSuccess()) {
            System.out.println(new Date() + ": 客户端登录成功");
            LoginUtil.markAsLogin(ctx.channel());
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
        }
    }
}

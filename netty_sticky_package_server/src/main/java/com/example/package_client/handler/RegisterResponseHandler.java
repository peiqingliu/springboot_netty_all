package com.example.package_client.handler;

import com.example.netty.code.request.RegisterRequestPacketUS;
import com.example.netty.code.response.RegisterResponsePacketUS;
import com.example.netty.code.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;


/**
 * @Author Liupeiqing
 * @Date 2020/2/7 18:10
 * @Version 1.0
 */
@Slf4j
public class RegisterResponseHandler extends SimpleChannelInboundHandler<RegisterResponsePacketUS> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 创建登录对象
        RegisterRequestPacketUS registerRequestPacketUS = new RegisterRequestPacketUS();
        registerRequestPacketUS.setDeviceId("00000000");
        byte address = 0x00;
        registerRequestPacketUS.setDeviceAddress(address);

        // 写数据
        ctx.channel().writeAndFlush(registerRequestPacketUS);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RegisterResponsePacketUS registerResponsePacketUS) throws Exception {
        log.info("获取响应");
        if (registerResponsePacketUS.isSuccess()) {
            log.info(new Date() + ": 客户端登录成功");
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + registerResponsePacketUS.getReason());
        }
    }
}

package com.exanple.package_server.handler;

import com.example.netty.code.request.RegisterRequestPacketUS;
import com.example.netty.code.response.RegisterResponsePacketUS;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Liupeiqing
 * @Date 2020/2/7 18:06
 * @Version 1.0
 */
@Slf4j
public class RegisterRequestHandler extends SimpleChannelInboundHandler<RegisterRequestPacketUS> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RegisterRequestPacketUS registerRequestPacketUS) throws Exception {
        log.info("收到客户端注册的请求");
        String deviceId = registerRequestPacketUS.getDeviceId();
        byte command = registerRequestPacketUS.getCommand();
        log.info("获取设备Id"+deviceId + "获取指令" + command);

        RegisterResponsePacketUS registerResponsePacketUS = new RegisterResponsePacketUS();
        registerResponsePacketUS.setSuccess(true);
        registerResponsePacketUS.setReason("验证成功");
        // 登录响应
        ctx.channel().writeAndFlush(registerResponsePacketUS);
    }
}

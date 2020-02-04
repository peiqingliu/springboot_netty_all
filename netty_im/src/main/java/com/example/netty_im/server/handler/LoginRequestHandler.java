package com.example.netty_im.server.handler;

import com.example.netty_im.protocol.request.LoginRequestPacket;
import com.example.netty_im.protocol.response.LoginResponsePacket;
import com.example.netty_im.session.Session;
import com.example.netty_im.util.SessionUtil;
import com.example.netty_im.util.SnowFlakeUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 0:02
 * @Version 1.0
 * 申请登录处理类
 */
@Slf4j
@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        loginResponsePacket.setUserName(loginRequestPacket.getUserName());
        if (valid(loginRequestPacket)){
            loginResponsePacket.setSuccess(true);
            String userId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());
            loginResponsePacket.setUserId(userId);
            log.info("[" + loginRequestPacket.getUserName() + "]登录成功");

            //绑定在channel上
            SessionUtil.bindSession(new Session(userId,loginRequestPacket.getUserName()),ctx.channel());
        }else {
            loginResponsePacket.setSuccess(false);
            loginResponsePacket.setReason("用户名或密码错误");
            log.info(loginRequestPacket.getUserName() + "登录失败"+ LocalDate.now());
        }

        // 登录响应
        ctx.writeAndFlush(loginResponsePacket);

    }

    /**
     * 验证登录
     * @param loginRequestPacket
     * @return
     */
    private boolean valid(LoginRequestPacket loginRequestPacket) {
        //省略验证部分
        return true;
    }

    /**
     *  断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
    }
}

package com.exanple.package_server.handler;

import com.example.netty.code.request.LoginRequestPacket;
import com.example.netty.code.response.LoginResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Date;

/**
 * @Author Liupeiqing
 * @Date 2020/2/6 22:51
 * @Version 1.0
 * SimpleChannelInboundHandler 对象，类型判断和对象传递的活都自动帮我们实现了，而我们可以专注于处理我们所关心的指令即可。
 */
@Slf4j
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    /**
     *
     * @param ctx
     * @param loginRequestPacket 该参数是有 父类中的方法 判断类型转换的
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {

        System.out.println(LocalDate.now() + ": 收到客户端登录请求……");

        //设置响应
        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setVersion(loginRequestPacket.getVersion());

        String userId = loginRequestPacket.getUserId();
        String userName = loginRequestPacket.getUserName();
        String pwd = loginRequestPacket.getPwd();
        log.info("获取客户端的信息" + userId + ":" + userName + ":" + pwd + ":" +  loginRequestPacket.getCommand());
        if (valid(loginRequestPacket)){
            //验证成功
            responsePacket.setSuccess(true);
            responsePacket.setReason("登录成功。");
            log.info("userName" + userName + LocalDate.now() +  ": 登录成功!");
        }else {
            responsePacket.setReason("账号密码校验失败");
            responsePacket.setSuccess(false);
            System.out.println(new Date() + ": 登录失败!");
        }

        // 登录响应
        ctx.channel().writeAndFlush(responsePacket);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}

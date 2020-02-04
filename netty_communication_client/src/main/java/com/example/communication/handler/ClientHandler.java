package com.example.communication.handler;

import com.example.communication.util.LoginUtil;
import com.example.netty_common.protocol.Packet;
import com.example.netty_common.protocol.PacketCodec;
import com.example.netty_common.protocol.request.LoginRequestPacket;
import com.example.netty_common.protocol.response.LoginResponsePacket;
import com.example.netty_common.protocol.response.MessageResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Date;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 17:14
 * @Version 1.0
 */
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端开始登陆" + LocalDate.now());

        // 1.创建数据包对象
        LoginRequestPacket requestPacket = new LoginRequestPacket();
        requestPacket.setUserName("liupeiqing");
        requestPacket.setPassword("123456");
        requestPacket.setVersion(new Byte("1"));

        //2.编码  将java对象转换成 二进制bao
        ByteBuf buffer = ctx.alloc().buffer();
        PacketCodec.INSTANCE.encode(buffer,requestPacket);
        String result = buffer.toString();

        //3 写数据
        ctx.channel().writeAndFlush(buffer);
    }

    /**
     * 读取服务端返回的数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        Packet packet = PacketCodec.INSTANCE.decode(byteBuf);

        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;

            if (loginResponsePacket.isSuccess()) {
                LoginUtil.markAsLogin(ctx.channel());
                System.out.println(new Date() + ": 客户端登录成功");
            } else {
                System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
            }
        }else if (packet instanceof MessageResponsePacket) {
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
            System.out.println(new Date() + ": 收到服务端的消息: " + messageResponsePacket.getMessage());
        }
    }
}

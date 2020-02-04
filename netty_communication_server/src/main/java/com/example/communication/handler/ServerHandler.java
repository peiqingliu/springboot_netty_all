package com.example.communication.handler;

import com.example.netty_common.protocol.Packet;
import com.example.netty_common.protocol.PacketCodec;
import com.example.netty_common.protocol.request.LoginRequestPacket;
import com.example.netty_common.protocol.request.MessageRequestPacket;
import com.example.netty_common.protocol.response.LoginResponsePacket;
import com.example.netty_common.protocol.response.MessageResponsePacket;
import com.example.netty_common.util.IDUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 17:25
 * @Version 1.0
 */
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        // 1.获取数据包转成buf
        ByteBuf byteBuf = (ByteBuf) msg;
        // 2.解码
        Packet packet = PacketCodec.INSTANCE.decode(byteBuf);

        //3.设置响应
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
       // loginResponsePacket.setVersion(packet.getVersion());

        if (packet instanceof LoginRequestPacket){
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            if (valid(loginRequestPacket)){
                //校验成功
                log.info("校验成功");
                loginResponsePacket.setSuccess(true);
                loginResponsePacket.setUserName(loginRequestPacket.getUserName());
                loginResponsePacket.setUserId(IDUtil.randomId());

            }else {
                log.info("登录失败");
                loginResponsePacket.setReason("账号密码校验失败");
                loginResponsePacket.setSuccess(false);
            }
            ByteBuf responseByteBuf = ctx.alloc().buffer();
            PacketCodec.INSTANCE.encode(responseByteBuf,loginResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }else if (packet instanceof MessageRequestPacket) {
            // 处理消息
            MessageRequestPacket messageRequestPacket = ((MessageRequestPacket) packet);
            System.out.println(LocalDate.now() + ": 收到客户端消息: " + messageRequestPacket.getMessage());

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");
            ByteBuf responseByteBuf = ctx.alloc().buffer();
            PacketCodec.INSTANCE.encode(responseByteBuf, messageResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }

    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}

package com.example.netty_im.server.handler;

import com.example.netty_im.protocol.request.MessageRequestPacket;
import com.example.netty_im.protocol.response.MessageResponsePacket;
import com.example.netty_im.session.Session;
import com.example.netty_im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 0:38
 * @Version 1.0
 * 发送消息请求 处理
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();

    private MessageRequestHandler() {

    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket requestPacket) throws Exception {
        long begin = System.currentTimeMillis();

        // 1.拿到消息发送方的会话信息
        Session session = SessionUtil.getSession(ctx.channel());

        // 2.通过消息发送方的会话信息，构造要发送的消息
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setFromUserName(session.getUserName());
        messageResponsePacket.setMessage(requestPacket.getMessage());

        // 3.拿到消息接收方的 channel
        String toUserId = requestPacket.getToUserId();
        Channel toUserChannel  = SessionUtil.getChannel(toUserId);

        // 4.将消息发送给消息接收方
        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)){
            toUserChannel.writeAndFlush(messageResponsePacket).addListener(future -> {
                if (future.isDone()){
                    log.info("发送成功");
                }
            });
        }else {
            log.info("[" + session.getUserId() + "] 不在线，发送失败!");
        }
    }
}

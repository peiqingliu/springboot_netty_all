package com.sample.netty_im_client.handler;

import com.example.netty_common.protocol.response.CreateGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 12:00
 * @Version 1.0
 */
@Slf4j
public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket responsePacket) throws Exception {

        log.info("群创建成功，id 为[" + responsePacket.getGroupId() + "], ");
        log.info("群里面有：" + responsePacket.getUserNameList());
    }
}

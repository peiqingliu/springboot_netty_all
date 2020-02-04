package com.example.netty_im.server.handler;

import com.example.netty_im.protocol.request.CreateGroupRequestPacket;
import com.example.netty_im.protocol.response.CreateGroupResponsePacket;
import com.example.netty_im.util.IDUtil;
import com.example.netty_im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 9:42
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    public static final CreateGroupRequestHandler INSTANCE = new CreateGroupRequestHandler();

    private CreateGroupRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket requestPacket) throws Exception {

        List<String> userIdList = requestPacket.getUserIdList();

        List<String> userNameList = new ArrayList<>();
        // 1. 创建一个 channel 分组  ChannelGroup是channel的集合
        //使用群组进行广播
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());

        // 2. 筛选出待加入群聊的用户的 channel 和 userName
        for (String userId : userIdList){
            Channel channel = SessionUtil.getChannel(userId);
            if (channel != null){
                channelGroup.add(channel);
                userNameList.add(SessionUtil.getSession(channel).getUserName());
            }
        }

        // 3. 创建群聊创建结果的响应
        String groupId = IDUtil.randomId();
        CreateGroupResponsePacket responsePacket = new CreateGroupResponsePacket();
        responsePacket.setGroupId(groupId);
        responsePacket.setSuccess(true);
        responsePacket.setUserNameList(userNameList);
        responsePacket.setVersion(requestPacket.getVersion());

        // 4. 给每个客户端发送拉群通知
        channelGroup.writeAndFlush(responsePacket);
        log.info("群创建成功，id 为 " + responsePacket.getGroupId() + ", ");
        log.info("群里面有：" + responsePacket.getUserNameList());

        // 5. 保存群组相关的信息
        SessionUtil.bindChannelGroup(groupId, channelGroup);

    }
}

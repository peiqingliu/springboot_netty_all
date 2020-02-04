package com.example.netty_im.protocol.response;

import com.example.netty_im.protocol.Packet;
import com.example.netty_im.session.Session;
import lombok.Data;

import static com.example.netty_im.protocol.command.Command.GROUP_MESSAGE_RESPONSE;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:30
 * @Version 1.0
 * 群聊消息 响应 数据包
 */
@Data
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;

    private Session fromUser;

    private String message;

    @Override
    public Byte getCommand() {
        return GROUP_MESSAGE_RESPONSE;
    }
}

package com.example.netty_im.protocol.request;

import com.example.netty_im.protocol.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.netty_im.protocol.command.Command.GROUP_MESSAGE_REQUEST;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:36
 * @Version 1.0
 * 发送群聊消息 请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessageRequestPacket extends Packet {

    private String toGroupId;
    private String message;

    @Override
    public Byte getCommand() {
        return GROUP_MESSAGE_REQUEST;
    }
}

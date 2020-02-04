package com.example.netty_common.protocol.response;

import com.example.netty_common.protocol.Packet;
import lombok.Data;

import static com.example.netty_common.protocol.command.Command.MESSAGE_RESPONSE;


/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:12
 * @Version 1.0
 * 服务端发消息 数据包
 */
@Data
public class MessageResponsePacket extends Packet {

    private String fromUserId;
    private String fromUserName;
    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_RESPONSE;
    }
}

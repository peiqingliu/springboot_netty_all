package com.example.netty_im.protocol.response;

import com.example.netty_im.protocol.Packet;
import lombok.Data;

import static com.example.netty_im.protocol.command.Command.JOIN_GROUP_RESPONSE;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:32
 * @Version 1.0
 * 加入群聊 相应
 */
@Data
public class JoinGroupResponsePacket extends Packet {

    private String groupId;
    private boolean success;
    private String reason;

    @Override
    public Byte getCommand() {
        return JOIN_GROUP_RESPONSE;
    }
}

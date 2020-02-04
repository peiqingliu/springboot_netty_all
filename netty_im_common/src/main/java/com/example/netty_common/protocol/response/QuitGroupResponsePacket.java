package com.example.netty_common.protocol.response;

import com.example.netty_common.protocol.Packet;
import lombok.Data;

import static com.example.netty_common.protocol.command.Command.QUIT_GROUP_RESPONSE;


/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:34
 * @Version 1.0
 */
@Data
public class QuitGroupResponsePacket extends Packet {
    private String groupId;

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {

        return QUIT_GROUP_RESPONSE;
    }
}

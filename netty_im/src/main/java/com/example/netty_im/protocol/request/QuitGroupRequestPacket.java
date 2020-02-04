package com.example.netty_im.protocol.request;

import com.example.netty_im.protocol.Packet;
import lombok.Data;

import static com.example.netty_im.protocol.command.Command.QUIT_GROUP_REQUEST;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:42
 * @Version 1.0
 */
@Data
public class QuitGroupRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {
        return QUIT_GROUP_REQUEST;
    }
}

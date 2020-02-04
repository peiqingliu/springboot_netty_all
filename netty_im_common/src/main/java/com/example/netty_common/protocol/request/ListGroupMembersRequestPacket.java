package com.example.netty_common.protocol.request;

import com.example.netty_common.protocol.Packet;
import lombok.Data;

import static com.example.netty_common.protocol.command.Command.LIST_GROUP_MEMBERS_REQUEST;


/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:39
 * @Version 1.0
 */
@Data
public class ListGroupMembersRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {
        return LIST_GROUP_MEMBERS_REQUEST;
    }
}

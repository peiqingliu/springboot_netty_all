package com.example.netty_common.protocol.response;

import com.example.netty_common.protocol.Packet;
import com.example.netty_common.session.Session;
import lombok.Data;

import java.util.List;

import static com.example.netty_common.protocol.command.Command.LIST_GROUP_MEMBERS_RESPONSE;


/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:33
 * @Version 1.0
 */
@Data
public class ListGroupMembersResponsePacket extends Packet {

    private String groupId;

    private List<Session> sessionList;

    @Override
    public Byte getCommand() {
        return LIST_GROUP_MEMBERS_RESPONSE;
    }
}

package com.example.netty_im.protocol.response;

import com.example.netty_im.protocol.Packet;
import com.example.netty_im.session.Session;
import lombok.Data;

import java.util.List;

import static com.example.netty_im.protocol.command.Command.LIST_GROUP_MEMBERS_RESPONSE;

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

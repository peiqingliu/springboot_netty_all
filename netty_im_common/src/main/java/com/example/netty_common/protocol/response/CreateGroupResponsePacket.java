package com.example.netty_common.protocol.response;

import com.example.netty_common.protocol.Packet;
import lombok.Data;

import java.util.List;

import static com.example.netty_common.protocol.command.Command.CREATE_GROUP_RESPONSE;


/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:22
 * @Version 1.0
 * 创建群组响应 数据包
 */
@Data
public class CreateGroupResponsePacket extends Packet {

    private boolean success;

    private String groupId;
    private List<String> userNameList;

    @Override
    public Byte getCommand() {
        return CREATE_GROUP_RESPONSE;
    }
}

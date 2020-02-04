package com.example.netty_im.protocol.request;

import com.example.netty_im.protocol.Packet;
import lombok.Data;

import java.util.List;

import static com.example.netty_im.protocol.command.Command.CREATE_GROUP_REQUEST;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:35
 * @Version 1.0
 * 创建群聊请求
 */
@Data
public class CreateGroupRequestPacket extends Packet {
    private List<String> userIdList;
    @Override
    public Byte getCommand() {
        return CREATE_GROUP_REQUEST;
    }
}

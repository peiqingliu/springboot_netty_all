package com.example.netty_im.protocol.request;

import com.example.netty_im.protocol.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.example.netty_im.protocol.command.Command.MESSAGE_REQUEST;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:40
 * @Version 1.0
 * 发送消息请求数据包
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestPacket extends Packet {

    private String toUserId;
    private String message;
    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}

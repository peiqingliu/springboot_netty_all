package com.example.netty_common.protocol.request;

import com.example.netty_common.protocol.Packet;
import lombok.Data;

import static com.example.netty_common.protocol.command.Command.LOGOUT_REQUEST;


/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:40
 * @Version 1.0
 */
@Data
public class LogoutRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return LOGOUT_REQUEST;
    }
}

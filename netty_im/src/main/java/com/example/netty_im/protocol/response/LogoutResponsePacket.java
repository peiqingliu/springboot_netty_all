package com.example.netty_im.protocol.response;

import com.example.netty_im.protocol.Packet;
import lombok.Data;

import static com.example.netty_im.protocol.command.Command.LOGOUT_RESPONSE;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:18
 * @Version 1.0
 * 登出相应 数据包
 */
@Data
public class LogoutResponsePacket extends Packet {

    private boolean success;
    private String reason;

    @Override
    public Byte getCommand() {
        return LOGOUT_RESPONSE;
    }
}

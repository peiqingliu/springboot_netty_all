package com.example.netty.code.response;

import com.example.netty.code.protocol.Packet;
import lombok.Data;

import static com.example.netty.code.command.Command.LOGIN_RESPONSE;

/**
 * @Author Liupeiqing
 * @Date 2020/2/6 22:56
 * @Version 1.0
 */
@Data
public class LoginResponsePacket extends Packet {

    private boolean success;

    private String reason;


    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}

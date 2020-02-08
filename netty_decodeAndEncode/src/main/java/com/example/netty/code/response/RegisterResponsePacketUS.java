package com.example.netty.code.response;

import com.example.netty.code.protocol.PacketUS;
import lombok.Data;

import static com.example.netty.code.command.Command.REGISTER_RESPONSE;

/**
 * @Author Liupeiqing
 * @Date 2020/2/7 19:08
 * @Version 1.0
 */
@Data
public class RegisterResponsePacketUS extends PacketUS {

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {
        return REGISTER_RESPONSE;
    }
}

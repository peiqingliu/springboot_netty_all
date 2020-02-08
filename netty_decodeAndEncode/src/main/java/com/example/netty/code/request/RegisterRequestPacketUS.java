package com.example.netty.code.request;

import com.example.netty.code.protocol.PacketUS;
import lombok.Data;
import lombok.ToString;

import static com.example.netty.code.command.Command.REGISTER_REQUEST;

/**
 * @Author Liupeiqing
 * @Date 2020/2/7 17:02
 * @Version 1.0
 * 注册指令
 */
@Data
@ToString
public class RegisterRequestPacketUS extends PacketUS {

    /**
     * 设备编号
     */
    private String deviceId;

    @Override
    public Byte getCommand() {
        return REGISTER_REQUEST;
    }
}

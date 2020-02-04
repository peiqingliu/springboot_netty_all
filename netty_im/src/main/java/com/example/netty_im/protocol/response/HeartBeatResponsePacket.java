package com.example.netty_im.protocol.response;

import com.example.netty_im.protocol.Packet;
import lombok.Data;

import static com.example.netty_im.protocol.command.Command.HEARTBEAT_RESPONSE;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:31
 * @Version 1.0
 * 心跳相应
 */
@Data
public class HeartBeatResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return HEARTBEAT_RESPONSE;
    }
}

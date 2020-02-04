package com.example.netty_im.protocol.request;

import com.example.netty_im.protocol.Packet;

import static com.example.netty_im.protocol.command.Command.HEARTBEAT_REQUEST;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:38
 * @Version 1.0
 */
public class HeartBeatRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return HEARTBEAT_REQUEST;
    }
}

package com.example.netty_common.protocol.request;

import com.example.netty_common.protocol.Packet;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static com.example.netty_common.protocol.command.Command.LOGIN_REQUEST;


/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:01
 * @Version 1.0
 */
@Slf4j
@Data
public class LoginRequestPacket extends Packet {

    private String userName;

    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}

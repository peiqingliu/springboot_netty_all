package com.example.netty.code.request;

import com.example.netty.code.protocol.Packet;
import lombok.Data;
import lombok.ToString;

import static com.example.netty.code.command.Command.LOGIN_REQUEST;

/**
 * @Author Liupeiqing
 * @Date 2020/2/6 20:51
 * @Version 1.0
 * 登录请求 数据包
 */
@Data
@ToString
public class LoginRequestPacket extends Packet {

    private String userId;

    private String userName;

    private String pwd;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}

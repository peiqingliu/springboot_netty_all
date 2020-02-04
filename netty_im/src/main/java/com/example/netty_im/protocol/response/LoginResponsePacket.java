package com.example.netty_im.protocol.response;

import com.example.netty_im.protocol.Packet;
import lombok.Data;

import static com.example.netty_im.protocol.command.Command.LOGIN_RESPONSE;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 22:05
 * @Version 1.0
 * 登录响应 数据包
 */
@Data
public class LoginResponsePacket extends Packet {

    private String userId;
    private String userName;
    private boolean success;
    private String reason;

    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}

package com.example.netty.code.command;

/**
 * @Author Liupeiqing
 * @Date 2020/2/6 20:50
 * @Version 1.0
 * 指令集合
 */
public interface Command {

    /**
     * 登录请求
     */
    byte LOGIN_REQUEST = 1;

    Byte LOGIN_RESPONSE = 2;

    Byte MESSAGE_REQUEST = 3;

    Byte MESSAGE_RESPONSE = 4;

    /**
     * 注册
     */
    byte REGISTER_REQUEST = 0x30;

    /**
     * 注册
     */
    byte REGISTER_RESPONSE = 0x31;



}

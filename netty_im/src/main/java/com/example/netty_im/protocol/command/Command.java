package com.example.netty_im.protocol.command;

/**
 * 指令集合
 * @Author Liupeiqing
 * @Date 2020/2/3 21:47
 * @Version 1.0
 * 指令列表
 *
 * 指令内容	    客户端	服务端
 *
 * 登录请求	    发送	    接收
 *
 * 登录响应	    接收	    发送
 *
 * 客户端发消息	发送	    接收
 * 服务端发消息	接收	    发送
 * 登出请求	    发送	    接收
 * 登出响应	    接收	    发送
 * 创建群聊请求	发送	    接收
 * 群聊创建成功通知接收    发送
 * 加入群聊请求	发送  	接收
 * 群聊加入通知	接收	    发送
 * 发送群聊消息	发送	    接收
 * 接收群聊消息	接收	    发送
 * 退出群聊请求	发送	    接收
 * 退出群聊通知	接收	    发送
 */
public interface Command {

    /**
     * 登录请求
     */
    Byte LOGIN_REQUEST = 1;

    /**
     * 登录响应
     */
    Byte LOGIN_RESPONSE = 2;

    /**
     * 客户端发消息
     */
    Byte MESSAGE_REQUEST = 3;

    /**
     * 服务端发消息
     */
    Byte MESSAGE_RESPONSE = 4;

    /**
     * 登出请求
     */
    Byte LOGOUT_REQUEST = 5;

    /**
     * 登出响应
     */
    Byte LOGOUT_RESPONSE = 6;

    /**
     * 创建群聊请求
     */
    Byte CREATE_GROUP_REQUEST = 7;

    /**
     * 群聊创建成功通知
     */
    Byte CREATE_GROUP_RESPONSE = 8;

    /**
     *
     */
    Byte LIST_GROUP_MEMBERS_REQUEST = 9;

    /**
     *
     */
    Byte LIST_GROUP_MEMBERS_RESPONSE = 10;

    /**
     * 加入群聊请求
     */
    Byte JOIN_GROUP_REQUEST = 11;

    /**
     * 群聊加入通知
     */
    Byte JOIN_GROUP_RESPONSE = 12;

    /**
     * 退出群聊请求
     */
    Byte QUIT_GROUP_REQUEST = 13;

    /**
     * 退出群聊通知
     */
    Byte QUIT_GROUP_RESPONSE = 14;

    Byte GROUP_MESSAGE_REQUEST = 15;

    Byte GROUP_MESSAGE_RESPONSE = 16;

    /**
     * 心跳请求
     */
    Byte HEARTBEAT_REQUEST = 17;

    /**
     * 心跳相应
     */
    Byte HEARTBEAT_RESPONSE = 18;
}

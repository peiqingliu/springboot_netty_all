package com.example.netty_common.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 21:17
 * @Version 1.0
 * 数据包
 * 定义一下客户端与服务端通信的 Java 对象
 */
@Data
public abstract  class Packet {

    /**
     * 协议版本
     */
    @JSONField(deserialize = false, serialize = false)
    private Byte version = 1;


    /**
     * 获取指令
     * @return
     */
    @JSONField(serialize = false)
    public abstract Byte getCommand();

}

package com.example.netty.code.protocol;

import lombok.Data;

/**
 * @Author Liupeiqing
 * @Date 2020/2/6 20:48
 * @Version 1.0
 * 数据包
 *     魔数 + 版本 + 序列化算法 +  指令 + 数据域长度 + 传递数据
 * 字节：4  +  1  +   1        +  1   +    4     +    n
 */

@Data
public abstract class Packet {

    /**
     * 协议版本
     */
    private byte version = 1;

    /**
     * 获取指令
     * @return
     */
    public abstract Byte getCommand();
}

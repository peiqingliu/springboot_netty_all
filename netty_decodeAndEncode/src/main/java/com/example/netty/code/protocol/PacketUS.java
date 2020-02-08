package com.example.netty.code.protocol;

import lombok.Data;

/**
 * @Author Liupeiqing
 * @Date 2020/2/7 17:01
 * @Version 1.0
 *
 * 名称	       起始符	帧长度	板地址	指令字	数据域	校验
 * 长度(字节)	    4	    1	    1	    1	    n	    1
 */
@Data
public abstract class PacketUS {

    /**
     * 板地址
     */
    private byte deviceAddress;

    /**
     * 校验字符
     */
    private byte validate;


    /**
     * 获取指令
     * @return
     */
    public abstract Byte getCommand();
}

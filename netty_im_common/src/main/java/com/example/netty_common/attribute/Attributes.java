package com.example.netty_common.attribute;

import com.example.netty_common.session.Session;
import io.netty.util.AttributeKey;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 20:12
 * @Version 1.0
 */
public interface Attributes {
    //实际上AttributeKey与ChannelOption非常类似，都是根据给定的名称获取一个常量。
    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}

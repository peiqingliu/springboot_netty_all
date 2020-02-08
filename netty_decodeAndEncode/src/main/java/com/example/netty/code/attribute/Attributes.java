package com.example.netty.code.attribute;

import io.netty.util.AttributeKey;

/**
 * @Author Liupeiqing
 * @Date 2020/2/6 23:31
 * @Version 1.0
 */
public interface Attributes {

    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}

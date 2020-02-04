package com.example.netty_im.serialize;

import com.example.netty_im.serialize.impl.JSONSerializer;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 21:20
 * @Version 1.0
 * 自定义序列化算法
 */
public interface  Serializer {

    Serializer DEFAULT = new JSONSerializer();

    /**
     * 序列化算法
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * java 对象转换成二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制转换成 java 对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
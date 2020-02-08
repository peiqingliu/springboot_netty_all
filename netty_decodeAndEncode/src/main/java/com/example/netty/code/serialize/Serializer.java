package com.example.netty.code.serialize;


import com.example.netty.code.serialize.impl.JSONSerializer;

/**
 * @Author Liupeiqing
 * @Date 2020/2/6 20:54
 * @Version 1.0
 * 序列化接口
 */
public interface Serializer {

    Serializer DEFAULT = new JSONSerializer();

    /**
     * json 序列化
     */
    byte JSON_SERIALIZER = 1;

    /**
     * 获取序列化算法，占一个字节
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * 将对象转成字节数组
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     * 将字节数组转成对象
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(Class<T> clazz,byte[] bytes);
}

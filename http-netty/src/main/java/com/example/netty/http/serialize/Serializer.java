package com.example.netty.http.serialize;


/**
 * 序列化接口
 */
public interface Serializer {

    /**
     * 讲对象序列化成 字节数组
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     *  将字节数组反序列化成对象
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(Class<T> clazz,byte[] bytes);
}

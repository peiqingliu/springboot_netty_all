package com.example.netty_common.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.example.netty_common.serialize.Serializer;
import com.example.netty_common.serialize.SerializerAlgorithm;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 21:21
 * @Version 1.0
 * 自定义 实现序列化
 */
public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    /**
     * 将对象转成字节数组
     * @param object
     * @return
     */
    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}

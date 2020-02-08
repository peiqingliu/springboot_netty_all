package com.example.netty.code.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.example.netty.code.serialize.Serializer;
import com.example.netty.code.serialize.SerializerAlgorithm;

/**
 * @Author Liupeiqing
 * @Date 2020/2/6 20:59
 * @Version 1.0
 * 序列化接口 实现
 */
public class JSONSerializer implements Serializer {
    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}

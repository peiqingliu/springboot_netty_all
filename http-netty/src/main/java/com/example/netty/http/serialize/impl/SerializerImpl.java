package com.example.netty.http.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.example.netty.http.serialize.Serializer;


/**
 * 我们使用最简单的 json 序列化方式，使用阿里巴巴的 fastjson 作为序列化框架。
 */
public class SerializerImpl implements Serializer {

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}

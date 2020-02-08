package com.example.netty.code.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author Liupeiqing
 * @Date 2020/2/7 18:01
 * @Version 1.0
 */
public class PacketDecoderUS extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        out.add(PacketCodecUS.INSTANCE.decode(in));
    }
}

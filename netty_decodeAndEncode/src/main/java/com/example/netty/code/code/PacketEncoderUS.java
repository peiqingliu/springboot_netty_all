package com.example.netty.code.code;

import com.example.netty.code.protocol.PacketUS;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author Liupeiqing
 * @Date 2020/2/7 17:59
 * @Version 1.0
 */
public class PacketEncoderUS extends MessageToByteEncoder<PacketUS> {
    @Override
    protected void encode(ChannelHandlerContext ctx, PacketUS packetUS, ByteBuf out) throws Exception {
        PacketCodecUS.INSTANCE.encode(out,packetUS);
    }
}

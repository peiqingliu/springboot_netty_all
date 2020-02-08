package com.example.netty.code.code;

import com.example.netty.code.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author Liupeiqing
 * @Date 2020/2/6 23:03
 * @Version 1.0
 * 而Netty 提供了一个特殊的 channelHandler 来专门处理编码逻辑，
 * 我们不需要每一次将响应写到对端的时候调用一次编码逻辑进行编码，
 * 也不需要自行创建 ByteBuf，这个类叫做 MessageToByteEncoder，从字面意思也可以看出，它的功能就是将对象转换到二进制数据。
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
        PacketCodec.INSTANCE.encode(out, packet);
    }
}

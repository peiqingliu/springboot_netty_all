package com.example.netty_im.codec;

import com.example.netty_im.protocol.Packet;
import com.example.netty_im.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 21:16
 * @Version 1.0
 * 编码
 * 而Netty 提供了一个特殊的 channelHandler 来专门处理编码逻辑，
 * 我们不需要每一次将响应写到对端的时候调用一次编码逻辑进行编码，也不需要自行创建 ByteBuf，
 * 这个类叫做 MessageToByteEncoder，从字面意思也可以看出，
 * 它的功能就是将对象转换到二进制数据。
 *
 * PacketEncoder 继承自 MessageToByteEncoder，泛型参数 Packet 表示这个类的作用是实现 Packet 类型对象到二进制的转换。
 *
 * 这里我们只需要实现 encode() 方法，我们注意到，在这个方法里面，
 * 第二个参数是 Java 对象，而第三个参数是 ByteBuf 对象，
 * 我们在这个方法里面要做的事情就是把 Java 对象里面的字段写到 ByteBuf，
 * 我们不再需要自行去分配 ByteBuf
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
        PacketCodec.INSTANCE.encode(out,packet);

    }
}

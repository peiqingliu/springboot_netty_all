package com.example.netty.code.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author Liupeiqing
 * @Date 2020/2/6 22:43
 * @Version 1.0
 * 实现服务端的解码
 * 当我们收到数据之后，首先要做的事情就是把二进制数据转换到我们的一个 Java 对象，所以 Netty 很贴心地写了一个父类，来专门做这个事情
 */
public class PacketDecoder extends ByteToMessageDecoder {

    /**
     *
     * @param channelHandlerContext  handler上下文
     * @param in 传递进来的时候就已经是 ByteBuf 类型，所以我们不再需要强转
     * @param out 第三个参数是 List 类型，
     *            我们通过往这个 List 里面添加解码后的结果对象，就可以自动实现结果往下一个 handler 进行传递
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        out.add(PacketCodec.INSTANCE.decode(in));
    }
}

package com.example.netty_common.codec;

import com.example.netty_common.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author Liupeiqing
 * @Date 2020/2/3 21:11
 * @Version 1.0
 * 自定义解码器
 */
@Slf4j
public class Spliter extends LengthFieldBasedFrameDecoder {

    private static final int LENGTH_FIELD_OFFSET = 7;
    private static final int LENGTH_FIELD_LENGTH = 4;

    public Spliter() {
        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    /**
     * 解码
     * @param ctx
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in.readableBytes() >= Integer.BYTES && in.getInt(in.readerIndex()) != PacketCodec.MAGIC_NUMBER) {
            log.info("如果输入的数字小于4个字符，此处会关闭连接，已修复");
            ctx.channel().close();
            return null;
        }

        return super.decode(ctx, in);
    }
}

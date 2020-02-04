package com.example.communication.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.time.LocalDate;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 16:02
 * @Version 1.0
 */
@Slf4j
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端写出数据" + LocalDate.now());

        // 1.获取数据
        ByteBuf byteBuf = getByteBuf(ctx);

        ctx.channel().writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        log.info("客户端收到服务端回复的数据:" + byteBuf.toString(Charset.forName("utf-8")));

    }

    /**
     * Netty 里面数据是以 ByteBuf 为单位的， 所有需要写出的数据都必须塞到一个 ByteBuf，
     * 数据的写出是如此，数据的读取亦是如此
     * @return
     */
    private ByteBuf getByteBuf(ChannelHandlerContext ctx){


        /**
         * ctx.alloc() 获取到一个 ByteBuf 的内存管理器，
         * 这个 内存管理器的作用就是分配一个 ByteBuf，
         * 然后我们把字符串的二进制数据填充到 ByteBuf，
         * 这样我们就获取到了 Netty 需要的一个数据格式
         */
        // 1.获取二进制抽象 ByteBuf
        ByteBuf byteBuf = ctx.alloc().buffer();

        // 2准备数据
        byte[] bytes = "你好，中国".getBytes(Charset.forName("utf-8"));

        // 3.填充数据
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }
}

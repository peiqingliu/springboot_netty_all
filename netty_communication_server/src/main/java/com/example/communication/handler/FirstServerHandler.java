package com.example.communication.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.time.LocalDate;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 16:11
 * @Version 1.0
 */
@Slf4j
public class FirstServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    /**
     * 这个方法在接收到客户端发来的数据之后被回调。
     * @param ctx
     * @param msg
     * @throws Exception
     * 这里的 msg 参数指的就是 Netty 里面数据读写的载体，
     * 为什么这里不直接是 ByteBuf，而需要我们强转一下，我们后面会分析到。
     * 这里我们强转之后，然后调用 byteBuf.toString() 就能够拿到我们客户端发过来的字符串数据。
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        log.info(LocalDate.now() + ": 服务端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));


        log.info("服务端回复数据");
        //回复数据给客户端
        ByteBuf responseBuf = getByteBuf(ctx);

        //数据从管道中流出
        ctx.channel().writeAndFlush(responseBuf);

    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx){
        // 1.获取bytebuf
        ByteBuf byteBuf = ctx.alloc().buffer();

        // 2.回复的数据 转成字节
        byte[] bytes = "你的信息我已收到，谢谢".getBytes();

        byteBuf.writeBytes(bytes);
        return byteBuf;



    }

}

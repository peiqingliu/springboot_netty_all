package com.example.package_client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * @Author Liupeiqing
 * @Date 2020/2/5 12:28
 * @Version 1.0
 */
public class PackageHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0;i<100;i++){
            ByteBuf byteBuf = getByteBuf(ctx);
            ctx.channel().writeAndFlush(byteBuf);
        }
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx){
        ByteBuf byteBuf = ctx.alloc().buffer();
        String requestStr = "窗前明月光，疑是地上霜，举头望明月，低头思故乡";
        byte[] bytes = requestStr.getBytes(Charset.forName("utf-8"));
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }
}

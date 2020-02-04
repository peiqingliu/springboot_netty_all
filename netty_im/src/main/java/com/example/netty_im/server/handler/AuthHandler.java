package com.example.netty_im.server.handler;

import com.example.netty_im.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 0:30
 * @Version 1.0
 */
@ChannelHandler.Sharable
public class AuthHandler extends ChannelInboundHandlerAdapter {

    public static final AuthHandler INSTANCE = new AuthHandler();

    private AuthHandler() {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //没有登录
        if (!SessionUtil.hasLogin(ctx.channel())){
            ctx.channel().close();
        }else {
            //已经登录则，删除该AuthHandler
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }
}

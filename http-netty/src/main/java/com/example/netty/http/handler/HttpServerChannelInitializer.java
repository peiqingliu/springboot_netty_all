package com.example.netty.http.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline cp = socketChannel.pipeline();
        //调用#new HttpServerCodec()方法，编解码器支持部分 HTTP 请求解析，
        // 比如 HTTP GET请求所传递的参数是包含在 uri 中的，因此通过 HttpRequest 既能解析出请求参数。
        /**
         * 或者使用HttpRequestDecoder & HttpResponseEncoder
         *
         * .addLast("decoder", new HttpRequestDecoder())   // 1
         * .addLast("encoder", new HttpResponseEncoder())  // 2
         *
         * HttpRequestDecoder 即把 ByteBuf 解码到 HttpRequest 和 HttpContent。
         * HttpResponseEncoder 即把 HttpResponse 或 HttpContent 编码到 ByteBuf。
         *
         * HttpServerCodec 即 HttpRequestDecoder 和 HttpResponseEncoder 的结合。
         */
        cp.addLast(new HttpServerCodec())
        /**
         * aggregator，消息聚合器（重要）。
         * 为什么能有FullHttpRequest这个东西，
         * 就是因为有他，HttpObjectAggregator，
         * 如果没有他，就不会有那个消息是FullHttpRequest的那段Channel，
         * 同样也不会有FullHttpResponse。
         * 如果我们将z'h
         * HttpObjectAggregator(512 * 1024)的参数含义是消息合并的数据大小，如此代表聚合的消息内容长度不超过512kb。
         */
        .addLast("aggregator", new HttpObjectAggregator(512 * 1024))    // 3
         //添加自己的处理器
        .addLast("handler", new HttpServerHandler());
    }
}

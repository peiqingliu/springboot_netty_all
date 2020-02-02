package com.example.netty.http.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.netty.http.entity.User;
import com.example.netty.http.serialize.impl.SerializerImpl;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import io.netty.util.AsciiString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.Charsets;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Handler需要声明泛型为<FullHttpRequest>，声明之后，只有msg为FullHttpRequest的消息才能进来。
 */
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private HttpHeaders headers;
    private HttpRequest request;
    private FullHttpRequest fullRequest;

    private static final String FAVICON_ICO = "/favicon.ico";
    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");
    private static final AsciiString CONNECTION = AsciiString.cached("Connection");
    private static final AsciiString KEEP_ALIVE = AsciiString.cached("keep-alive");


    /**
     * 异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 传输完成
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        User user = new User();
        user.setUserName("xioaming");
        user.setDate(new Date());
        if (msg instanceof HttpRequest){
            request = (HttpRequest) msg;
            headers = request.headers();
            String uri = request.uri();
            log.info("uri:" + uri);
            if (uri.equals(FAVICON_ICO)){
                return;
            }
            HttpMethod method = request.method();
            if (method.equals(HttpMethod.GET)){
                //QueryStringDecoder 的作用就是把 HTTP uri 分割成 path 和 key-value 参数对
                QueryStringDecoder queryStringDecoder =
                        new QueryStringDecoder(uri, Charsets.toCharset(CharEncoding.UTF_8));
                Map<String, List<String>> uriAttributes  = queryStringDecoder.parameters();
                //此处仅打印请求参数（你可以根据业务需求自定义处理）
                for (Map.Entry<String,List<String>> attr : uriAttributes.entrySet()){
                    for (String s : attr.getValue()){
                        log.info("参数" + attr.getKey()+":"+s);
                    }
                }
                user.setMethod("get");
            }else if (method.equals(HttpMethod.POST)){
                //POST请求,由于你需要从消息体中获取数据,因此有必要把msg转换成FullHttpRequest
                fullRequest =  (FullHttpRequest) msg;

                //根据不同的Content_Type处理body数据
                dealWithContentType();
                user.setMethod("post");
            }

            SerializerImpl serializer = new SerializerImpl();
            //将Java对象序列化成为二级制数据包
            byte[] content = serializer.serialize(user);
            FullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK, Unpooled.wrappedBuffer(content));
            response.headers().set(CONTENT_TYPE,"text/plain");
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
            boolean keepAlive = HttpUtil.isKeepAlive(request);
            if (!keepAlive){
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }else {
                response.headers().set(CONNECTION,KEEP_ALIVE);
                ctx.write(response);
            }
        }

    }


    private void dealWithContentType(){
        String contentType = getContentType();
        //可以使用HttpJsonDecoder
        if (contentType.equals("application/json")){
            String jsonStr = fullRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
            JSONObject obj = JSON.parseObject(jsonStr);
            for(Map.Entry<String, Object> item : obj.entrySet()){
                log.info(item.getKey()+"="+item.getValue().toString());
            }

        }else if(contentType.equals("application/x-www-form-urlencoded")){
            //方式一：使用 QueryStringDecoder
            String jsonStr = fullRequest.content().toString(Charsets.toCharset(CharEncoding.UTF_8));
            QueryStringDecoder queryDecoder = new QueryStringDecoder(jsonStr, false);
            Map<String, List<String>> uriAttributes = queryDecoder.parameters();
            for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
                for (String attrVal : attr.getValue()) {
                    log.info(attr.getKey() + "=" + attrVal);
                }
            }

        }else if(contentType.equals("multipart/form-data")){
            //TODO 用于文件上传
        }else{
            //do nothing...
        }
    }

    private String getContentType(){
        String typeStr = headers.get("Content-Type").toString();
        String[] list = typeStr.split(";");
        return list[0];

    }
}

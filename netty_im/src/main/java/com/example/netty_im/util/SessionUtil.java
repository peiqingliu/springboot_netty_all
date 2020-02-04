package com.example.netty_im.util;

import com.example.netty_im.attribute.Attributes;
import com.example.netty_im.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 20:07
 * @Version 1.0
 */
@Slf4j
public class SessionUtil {

    //存放个人连接
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>(16);

    private static final Map<String, ChannelGroup> groupIdChannelGroupMap = new ConcurrentHashMap<>();

    /**
     * 绑定session
     * @param session
     * @param channel
     */
    public static void bindSession(Session session, Channel channel){
        userIdChannelMap.put(session.getUserId(),channel);
        //AttributeMap这是是绑定在Channel或者ChannelHandlerContext上的一个附件
        /**
         * 我们知道每一个ChannelHandlerContext都是ChannelHandler和ChannelPipeline之间连接的桥梁，
         * 每一个ChannelHandlerContext都有属于自己的上下文，
         * 也就说每一个ChannelHandlerContext上如果有AttributeMap都是绑定上下文的，
         * 也就说如果A的ChannelHandlerContext中的AttributeMap，B的ChannelHandlerContext是无法读取到的
         *
         * 但是Channel上的AttributeMap就是大家共享的，每一个ChannelHandler都能获取到
         * AttributeMap的结构：
         *
         *
         *
         * 可以看出这个是线程安全的，所以我们可以放心使用，
         * 再看看AttributeMap的结构，其实和Map的格式很像，
         * key是AttributeKey，value是Attribute，我们可以根据AttributeKey找到对应的Attribute，
         * 并且我们可以指定Attribute的类型T：
         */

        //channel上绑定自定义属性信息
        channel.attr(Attributes.SESSION).set(session);
    }

    //取消绑定
    public static void unBindSession(Channel channel){
        if (hasLogin(channel)){
            Session session = getSession(channel);
            //从map中移除
            userIdChannelMap.remove(session.getUserId());
            channel.attr(Attributes.SESSION).set(null);
            log.info(session + ": 退出登录！");
        }
    }

    /**
     * 判断是否登录
     * @param channel
     * @return
     */
    public static boolean hasLogin(Channel channel){
        return  getSession(channel) != null;
    }

    /**
     * 从channel上获取session
     * @param channel
     * @return
     */
    public static Session getSession(Channel channel){
        return  channel.attr(Attributes.SESSION).get();
    }

    public static void bindChannelGroup(String groupId,ChannelGroup channelGroup){
        groupIdChannelGroupMap.put(groupId,channelGroup);
    }

    public static ChannelGroup getChannelGroup(String groupId) {
        return groupIdChannelGroupMap.get(groupId);
    }

    public static Channel getChannel(String userId){
        return userIdChannelMap.get(userId);
    }
}

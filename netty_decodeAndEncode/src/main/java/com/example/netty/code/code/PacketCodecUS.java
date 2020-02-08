package com.example.netty.code.code;

import com.example.netty.code.protocol.PacketUS;
import com.example.netty.code.request.RegisterRequestPacketUS;
import com.example.netty.code.response.RegisterResponsePacketUS;
import com.example.netty.code.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.example.netty.code.command.Command.*;

/**
 * @Author Liupeiqing
 * @Date 2020/2/7 17:08
 * @Version 1.0
 */

@Slf4j
public class PacketCodecUS {

    public static final PacketCodecUS INSTANCE = new PacketCodecUS();

    /**
     * 起始符
     */
    private static final byte MAGIC_NUMBER1 = 0x6E;
    private static final byte MAGIC_NUMBER2 = 0x69;
    private static final byte MAGIC_NUMBER3 = 0x63;
    private static final byte MAGIC_NUMBER4 = 0x6b;


    private final Map<Byte, Class<? extends PacketUS>> packetTypeMap;

    public PacketCodecUS() {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(REGISTER_REQUEST, RegisterRequestPacketUS.class);
        packetTypeMap.put(REGISTER_RESPONSE, RegisterResponsePacketUS.class);
    }


    /**
     * 编码：封装成二进制的过程
     * @param packetUS
     * @return
     */
    public void encode(ByteBuf byteBuf, PacketUS packetUS){

        // 首先序列化对象
        byte[] bytes = Serializer.DEFAULT.serialize(packetUS);

        //1.先塞进去一个起始符
        byteBuf.writeByte(MAGIC_NUMBER1);
        byteBuf.writeByte(MAGIC_NUMBER2);
        byteBuf.writeByte(MAGIC_NUMBER3);
        byteBuf.writeByte(MAGIC_NUMBER4);
        //2.塞进去一个帧长度
        int length = bytes.length;
        byteBuf.writeByte(length);
        //3. 塞进去一版地址
        byteBuf.writeByte(packetUS.getDeviceAddress());
        //4. 塞进去指令
        byteBuf.writeByte(packetUS.getCommand());
        //5.将数据塞进去
        byteBuf.writeBytes(bytes);

    }

    /**
     * 解码：解析 Java 对象的过程
     */
    public PacketUS decode(ByteBuf byteBuf){

        //1.跳过魔数
        byteBuf.skipBytes(4);

        //2.获取数据长度
        byte length = byteBuf.readByte();
        log.info("获取注册数据长度" + length);

        // 代码片段二
       // byteBuf.markReaderIndex();

        //获取版地址
        byte address = byteBuf.readByte();

        // 获取指令
        byte command = byteBuf.readByte();

       // byteBuf.resetReaderIndex();

        //6.获取真正的数据
        //创建一个真实数据长度的 字节数组
        byte[] bytes = new byte[length];
        //把byteBUf中的数据 读到字节数组中
        byteBuf.readBytes(bytes);

        //根据指令获取 数据包对象
        Class<? extends PacketUS> clazz = getRequestType(command);
        //7.把字节转成对象
        PacketUS packet = Serializer.DEFAULT.deserialize(clazz,bytes);
        return packet;
    }

    private Class<? extends PacketUS> getRequestType(byte command){
        return packetTypeMap.get(command);
    }
}

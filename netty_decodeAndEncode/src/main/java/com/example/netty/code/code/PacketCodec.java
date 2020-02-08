package com.example.netty.code.code;

import com.example.netty.code.protocol.Packet;
import com.example.netty.code.request.LoginRequestPacket;
import com.example.netty.code.response.LoginResponsePacket;
import com.example.netty.code.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.example.netty.code.command.Command.LOGIN_REQUEST;
import static com.example.netty.code.command.Command.LOGIN_RESPONSE;

/**
 * @Author Liupeiqing
 * @Date 2020/2/6 21:59
 * @Version 1.0
 */
@Slf4j
public class PacketCodec {

    public static final PacketCodec INSTANCE = new PacketCodec();

    private static final int MAGIC_NUMBER = 0x12345678;

    private final Map<Byte, Class<? extends Packet>> packetTypeMap;

    public PacketCodec() {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
    }

    /**
     * 编码：封装成二进制的过程
     * @param packet
     * @return
     */
    public void encode(ByteBuf byteBuf,Packet packet){

        // 首先序列化对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        /**
         * 调用 Netty 的 ByteBuf 分配器来创建，
         * ioBuffer() 方法会返回适配 io 读写相关的内存，
         * 它会尽可能创建一个直接内存，直接内存可以理解为不受 jvm 堆管理的内存空间，写到 IO 缓冲区的效果更高。
         */
//        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        log.info("使用传递进来的ByteBuf");
        //1.先塞进去一个魔数 writeInt（）写一个int类型的数
        byteBuf.writeInt(MAGIC_NUMBER);
        //2.塞进去一个 版本 writeByte()写一个字节
        byteBuf.writeByte(packet.getVersion());
        //3. 塞进去一个算法
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        //4. 塞进去指令
        byteBuf.writeByte(packet.getCommand());

        //数据包的长度
        int length = bytes.length;
        //5.将数据长度塞进去
        byteBuf.writeInt(length);
        //6.将数据塞进去
        byteBuf.writeBytes(bytes);

        //return byteBuf;
    }

    /**
     * 解码：解析 Java 对象的过程
     */
    public Packet decode(ByteBuf byteBuf){

        //1.跳过魔数
        byteBuf.skipBytes(4);

        //2.跳过版本号
        byteBuf.skipBytes(1);

        //3.获取 序列化算法标识 readByte（）读取一个字节
        byte serializeAlgorithm = byteBuf.readByte();

        // 4.获取指令
        byte command = byteBuf.readByte();
        log.info("获取指令" + command);

        //5.获取数据长度
        int length = byteBuf.readInt();
        log.info("获取数据长度" + length);

        //6.获取真正的数据
        //创建一个真实数据长度的 字节数组
        byte[] bytes = new byte[length];
        //把byteBUf中的数据 读到字节数组中
        byteBuf.readBytes(bytes);

        //根据指令获取 数据包对象
        Class<? extends Packet> clazz = getRequestType(command);
        //7.把字节转成对象
        Packet packet = Serializer.DEFAULT.deserialize(clazz,bytes);
        return packet;
    }

    private Class<? extends Packet> getRequestType(byte command){
        return packetTypeMap.get(command);
    }
}

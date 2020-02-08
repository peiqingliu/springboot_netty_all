package com.example.netty_common.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 22:39
 * @Version 1.0
 */
@Slf4j
public class ByteBufTest {

    public static void main(String[] args) {
        /**
         * 创建一个容量为9个字节，最大100字节的buffer
         */
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9,100);
        print("allocate ByteBuf(9, 100)", buffer);

        /**
         * 向buffer中写入4个字节
         */
        // write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写
        buffer.writeBytes(new byte[]{1,2,3,4});
       // buffer.writeBytes(new byte[]{5,6,7,8,9});
        print("writeBytes(1,2,3,4)", buffer);

        // 设置写指针writeIndex(int)
        // write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写, 写完 int 类型 (12占4个字节)之后，写指针增加4
        buffer.writeInt(12);
        print("writeInt(12)", buffer);

        // write 方法改变写指针, 写完之后写指针等于 capacity 的时候，buffer 不可写
        buffer.writeBytes(new byte[]{5});
        print("writeBytes(5)", buffer);

        // write 方法改变写指针，写的时候发现 buffer 不可写则开始扩容，扩容之后 capacity 随即改变
        buffer.writeBytes(new byte[]{6});
        print("writeBytes(6)", buffer);

        // get 方法不改变读写指针
        System.out.println("getByte(3) return: " + buffer.getByte(3));
        System.out.println("getShort(3) return: " + buffer.getShort(3));
        System.out.println("getInt(3) return: " + buffer.getInt(3));
        print("getByte()", buffer);

        // set 方法不改变读写指针
        buffer.setByte(buffer.readableBytes() + 1, 0);
        print("setByte()", buffer);

        // read 方法改变读指针
        byte[] dst = new byte[buffer.readableBytes()];
        buffer.readBytes(dst);
        print("readBytes(" + dst.length + ")" + dst, buffer);
    }

    private static void print(String action, ByteBuf buffer) {
        System.out.println("after ===========" + action + "============");
        System.out.println("buffer的容量：capacity():" + buffer.capacity());
        System.out.println("buffer的最大容量maxCapacity(): " + buffer.maxCapacity());
        System.out.println("表示返回当前的读指针 readerIndex readerIndex(): " + buffer.readerIndex());
        System.out.println("表示 ByteBuf 当前可读的字节数 readableBytes():" + buffer.readableBytes());
        System.out.println("表示当前ByteBuf 是否可读" + buffer.isReadable());
        System.out.println("表示返回当前的写指针 writerIndex(): " + buffer.writerIndex());
        System.out.println("表示 ByteBuf 当前可写的字节数 writableBytes(): " + buffer.writableBytes());
        System.out.println("表示当前ByteBuf 是否可写 isWritable(): " + buffer.isWritable());
        System.out.println("可写的最大字节数，它的值等于 maxCapacity-writerIndex maxWritableBytes(): " + buffer.maxWritableBytes());
        System.out.println();

    }
}

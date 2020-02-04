package com.example.netty_common.util;

import java.util.UUID;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 20:04
 * @Version 1.0
 */
public class IDUtil {

    public static String randomId() {
        return UUID.randomUUID().toString().split("-")[0];
    }
}

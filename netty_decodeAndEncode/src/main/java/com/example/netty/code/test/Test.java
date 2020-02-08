package com.example.netty.code.test;

/**
 * @Author Liupeiqing
 * @Date 2020/2/8 0:16
 * @Version 1.0
 */
public class Test {

    public static void main(String[] args) {
        strTo16("00000000");
    }

    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }
}

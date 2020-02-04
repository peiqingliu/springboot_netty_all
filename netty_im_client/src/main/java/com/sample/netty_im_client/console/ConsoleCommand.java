package com.sample.netty_im_client.console;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @Author Liupeiqing
 * @Date 2020/2/4 13:08
 * @Version 1.0
 */
public interface ConsoleCommand {
    void exec(Scanner scanner, Channel channel);
}

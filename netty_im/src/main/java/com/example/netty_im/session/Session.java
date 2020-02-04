package com.example.netty_im.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author Liupeiqing
 * @Date 2020/2/3 20:09
 * @Version 1.0
 * 用户
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Session {

    // 用户唯一性标识
    private String userId;
    private String userName;
}

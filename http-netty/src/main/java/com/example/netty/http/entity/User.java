package com.example.netty.http.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class User {

    private String userName;

    private String method;

    private Date date;
}

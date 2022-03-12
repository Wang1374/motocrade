package com.laogeli.chatim.constant;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: 常量
 *
 * @author HongPei
 * @date 2021/5/29 15:11
 */
public class Constant {

    public static Map<String, WebSocketServerHandshaker> webSocketHandshakerMap = new ConcurrentHashMap<>();

    public static Map<String, Channel> onlineUserMap = new ConcurrentHashMap<>();

    public static String[] clients = new String[]{"ios", "android", "web", "win", "mac"};

}

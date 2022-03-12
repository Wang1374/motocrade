package com.laogeli.order.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// ServerEndpoint它的功能主要是将目前的类定义成一个websocket服务器端。注解的值将被用于监听用户连接的终端访问URL地址。
@ServerEndpoint(value = "/websocket/{userId}")
@Component
public class WebSocketServer {

    // 使用slf4j打日志
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    // 用来记录当前在线连接数
    private static final AtomicInteger onlineCount = new AtomicInteger(0);

    // 用来存放每个客户端对应的WebSocketServer对象
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<String, WebSocketServer>();

    // 某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    // 客户端的用户ID
    private String userId;

    /**
     * 连接建立成功,调用的方法,与前台页面的onOpen相对应
     *
     * @param userId  用户id
     * @param session 会话
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        //根据业务,自定义逻辑实现
        this.session = session;
        this.userId = userId;
        webSocketMap.put(userId, this);  // 将当前对象放入map中
        int count = onlineCount.incrementAndGet(); // 在线数加1
        log.info("有新的连接加入,userId:{}!当前在线人数:{}", userId, count);
    }

    /**
     * 连接关闭调用的方法,与前台页面的onClose相对应
     *
     * @param userId
     */
    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        webSocketMap.remove(userId);  // 根据userId(key)移除WebSocketServer对象
        int count = onlineCount.decrementAndGet(); // 在线数减1
        log.info("WebSocket关闭,userId:{},当前在线人数:{}", userId, count);
    }

    /**
     * 当服务器接收到客户端发送的消息时所调用的方法,与前台页面的onMessage相对应
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        // 根据业务,自定义逻辑实现
        log.info("来自客户端的消息:{}", message);
    }

    /**
     * 发生错误时调用,与前台页面的onError相对应
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误：{}，Session ID： {}", error.getMessage(), session.getId());
        error.printStackTrace();
    }

    /**
     * 根据userId发送消息给指定用户
     *
     * @param message
     */
    public static void sendToUser(String message) {
        String sendUserId = message.split(",")[1];
        String sendMessage = message.split(",")[0];
        if (webSocketMap.get(sendUserId) != null) {
            webSocketMap.get(sendUserId).sendMessage("用户" + sendUserId + "发来消息：" + sendMessage);
        } else {
            System.err.println("当前用户不在线");
        }
    }

    /**
     * 发送消息。实践表明，每次浏览器刷新，session会发生变化。
     *
     * @param message
     */
    public void sendMessage(String message) {
        try {
            // getBasicRemote()是同步发送消息,这里我就用这个了，推荐大家使用getAsyncRemote()异步
            this.session.getBasicRemote().sendText(message);
            log.info("推送消息成功，消息为：" + message);
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 给所有用户发消息
     *
     * @param message
     */
    public static void sendMessageAll(final String message) {
        System.err.println(webSocketMap.size());
        // 使用entrySet而不是用keySet的原因是,entrySet体现了map的映射关系,遍历获取数据更快。
        Set<Map.Entry<String, WebSocketServer>> entries = webSocketMap.entrySet();
        for (Map.Entry<String, WebSocketServer> entry : entries) {
            System.err.println(entry.getValue());
            final WebSocketServer webSocketServer = entry.getValue();
            // 这里使用线程来控制消息的发送,这样效率更高。
            new Thread(new Runnable() {
                public void run() {
                    webSocketServer.sendMessage(message);
                }
            }).start();
        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public static ConcurrentHashMap<String, WebSocketServer> getWebSocketMap() {
        return webSocketMap;
    }

    public static void setWebSocketMap(ConcurrentHashMap<String, WebSocketServer> webSocketMap) {
        WebSocketServer.webSocketMap = webSocketMap;
    }


        /*// 获取WebSocketServer对象的映射。
        ConcurrentHashMap<String, WebSocketServer> map = WebSocketServer.getWebSocketMap();
        if (map.size() != 0) {
            WebSocketServer.sendToUser("200,738701429283360768");
            System.err.println("向指定用户推送一次消息");
        } else {
            System.err.println("WebSocket未连接");
        }*/

        /*// 获取WebSocketServer对象的映射。
        ConcurrentHashMap<String, WebSocketServer> map = WebSocketServer.getWebSocketMap();
        if (map.size() != 0) {
            WebSocketServer.sendMessageAll("200");
            System.err.println("每隔2分钟,向客户端推送一次群发消息");
        } else {
            System.err.println("WebSocket未连接");
        }*/
}

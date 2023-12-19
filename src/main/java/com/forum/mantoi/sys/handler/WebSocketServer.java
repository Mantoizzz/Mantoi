package com.forum.mantoi.sys.handler;


import com.alibaba.fastjson.JSON;
import com.forum.mantoi.utils.JwtUtilities;
import com.forum.mantoi.utils.RedisKeys;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@AllArgsConstructor
@Component
@ServerEndpoint(value = "/chat/{token}")
public class WebSocketServer {

    private final JwtUtilities jwtUtilities;


    private final StringRedisTemplate stringRedisTemplate;
    /*
    Key:User's email
    Value:User's session
     */
    private static final ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 用户开启WebSocket
     *
     * @param session session
     * @param token   携带token
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "token") String token) {
        String email = jwtUtilities.extractEmail(token);
        session.getUserProperties().put("email", email);
        sessionMap.put(email, session);
        String redisKey = RedisKeys.getWebsocketListKey(email);

        List<String> msgList = stringRedisTemplate.opsForList().range(redisKey, 0, -1);
        if (msgList != null && !msgList.isEmpty()) {
            for (String msg : msgList) {
                Session target = sessionMap.get(email);
                sendMessage(target, msg);
            }
            stringRedisTemplate.delete(redisKey);
        }
    }

    /**
     * 客户端发来Message的时候干的事
     *
     * @param msg     msg
     * @param session session
     */
    @OnMessage
    public void onMessage(String msg, Session session) {
        String email = (String) session.getUserProperties().get("email");
        log.info("Websocket:用户发来消息:{}", email);
        Message message = JSON.parseObject(msg, Message.class);
        Session target = sessionMap.get(message.getTargetEmail());
        if (target == null) {
            String redisKey = RedisKeys.getWebsocketListKey(message.getTargetEmail());
            stringRedisTemplate.opsForList().rightPush(redisKey, message.getContent());
            return;
        }
        sendMessage(target, message.getContent());

    }

    /**
     * 关闭连接
     *
     * @param session session
     * @param token   token
     */
    @OnClose
    public void onClose(Session session, @PathParam(value = "token") String token) {
        String email = (String) session.getUserProperties().get("email");
        log.info("WebSocket:User {} is closing", email);
        sessionMap.remove(email);
    }

    @OnError
    public void onError(Throwable throwable, Session session) {
        log.info("WebSocket:{}", throwable.getMessage());
        String email = (String) session.getUserProperties().get("email");
        sessionMap.remove(email);
    }


    /**
     * 发送消息
     *
     * @param target 目标session
     * @param msg    消息
     */
    private void sendMessage(Session target, String msg) {
        try {
            target.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            log.info("WebSocket:Exception:{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 约定的Message格式
     */
    @Data
    private static class Message {

        String content;

        String targetEmail;
    }

}

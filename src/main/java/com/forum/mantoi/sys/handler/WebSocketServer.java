package com.forum.mantoi.sys.handler;


import com.alibaba.fastjson.JSON;
import com.forum.mantoi.utils.JwtUtilities;
import com.forum.mantoi.utils.RedisKeys;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
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

    private static final ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

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
    //TODO:@OnClose和@OnError需要补全


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

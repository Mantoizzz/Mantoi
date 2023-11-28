package com.forum.mantoi.sys.services;

import com.forum.mantoi.common.payload.MessageRequest;
import com.forum.mantoi.sys.entity.Message;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public void sendMessage(User to, MessageRequest request) {

    }


}

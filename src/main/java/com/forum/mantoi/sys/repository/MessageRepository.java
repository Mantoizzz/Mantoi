package com.forum.mantoi.sys.repository;

import com.forum.mantoi.sys.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}

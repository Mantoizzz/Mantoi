package com.forum.mantoi.sys.repository;

import com.forum.mantoi.sys.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

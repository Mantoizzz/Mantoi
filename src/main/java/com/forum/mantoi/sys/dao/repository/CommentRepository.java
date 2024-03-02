package com.forum.mantoi.sys.dao.repository;

import com.forum.mantoi.sys.dao.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(long commentId);
}

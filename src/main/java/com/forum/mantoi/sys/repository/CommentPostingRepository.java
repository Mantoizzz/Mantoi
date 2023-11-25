package com.forum.mantoi.sys.repository;

import com.forum.mantoi.sys.entity.CommentPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentPostingRepository extends JpaRepository<CommentPost, Long> {
}

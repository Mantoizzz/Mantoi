package com.forum.mantoi.sys.repository;

import com.forum.mantoi.sys.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PostingRepository extends JpaRepository<Post, Long> {
    Optional<Post> findPostById(Long id);
}

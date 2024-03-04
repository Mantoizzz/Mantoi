package com.forum.mantoi.sys.dao.repository;

import com.forum.mantoi.sys.dao.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Deprecated
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findPostById(Long id);

    @Modifying
    @Transactional
    @Query("update Post p set p.score=:score where p.id=:id")
    int updatePostScore(@Param("id") Long id, @Param("score") double score);

    List<Post> findTop25ByOrderByScoreDesc();
}

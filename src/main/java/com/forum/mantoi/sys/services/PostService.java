package com.forum.mantoi.sys.services;

import com.forum.mantoi.common.pojo.request.DeletePostDto;
import com.forum.mantoi.common.pojo.request.PublishPostDto;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.Comment;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.PostContent;
import com.forum.mantoi.sys.dao.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author DELL
 */
public interface PostService {

    public RestResponse<Void> publish(PublishPostDto dto);

    public RestResponse<Void> delete(DeletePostDto dto);

    public Post findById(Long id);

    public List<Post> getTopPosts();

    public Page<Post> findAll(Pageable pageable);

    public void updateScore(Long postId, double score);

    public User getAuthor(Post post);

    public List<Comment> getComments(Post post);

}

package com.forum.mantoi.sys.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.mantoi.common.pojo.dto.request.DeletePostDto;
import com.forum.mantoi.common.pojo.dto.request.PublishPostDto;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.Comment;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.User;

import java.util.List;

/**
 * @author DELL
 */
public interface PostService {

    public RestResponse<Void> publish(PublishPostDto dto);

    public RestResponse<Void> delete(DeletePostDto dto);

    public Post findById(Long id);

    public List<Post> getTopPosts();

    public Page<Post> findPosts(int size, int page);

    public void updateScore(Long postId, double score);

    public User getAuthor(Post post);

    public List<Comment> getComments(Post post);

}

package com.forum.mantoi.sys.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.mantoi.common.pojo.dto.request.DeletePostDto;
import com.forum.mantoi.common.pojo.dto.request.PostInformationDto;
import com.forum.mantoi.common.pojo.dto.request.PublishPostDto;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.Comment;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.PostContent;
import com.forum.mantoi.sys.dao.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

/**
 * @author DELL
 */
public interface PostService {

    RestResponse<Void> publish(PublishPostDto dto) throws IOException;

    RestResponse<Void> delete(DeletePostDto dto);

    Post findById(Long id);

    List<Post> getTopPosts();

    Page<Post> findPosts(int size, int page, HttpServletRequest request);

    void updateScore(Long postId, double score);

    User getAuthor(Post post);

    PostContent getContent(Post post);

    PostInformationDto getPostFromDatabase(long postId);

    PostInformationDto getPostDetail(long postId);
}

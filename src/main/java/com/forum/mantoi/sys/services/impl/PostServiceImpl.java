package com.forum.mantoi.sys.services.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.forum.mantoi.common.pojo.dto.request.DeletePostDto;
import com.forum.mantoi.common.pojo.dto.request.PublishPostDto;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.Comment;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.PostContent;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.dao.mapper.CommentMapper;
import com.forum.mantoi.sys.dao.mapper.PostContentMapper;
import com.forum.mantoi.sys.dao.mapper.PostMapper;
import com.forum.mantoi.sys.dao.mapper.UserMapper;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.services.PostService;
import com.forum.mantoi.sys.services.SearchService;
import com.forum.mantoi.utils.TokenBucketLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


/**
 * @author DELL
 */
@Service("postService")
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    private final UserMapper userMapper;

    private final CommentMapper commentMapper;

    private final PostContentMapper postContentMapper;

    private final SearchService searchService;

    private final TokenBucketLimiter limiter;


    @Override
    public RestResponse<Void> publish(PublishPostDto dto) throws IOException {
        Post post = Post.builder()
                .authorId(dto.getAuthor().getId())
                .title(dto.getTitle())
                .shortContent(dto.content.substring(0, 25))
                .likes(0)
                .score(0D)
                .build();
        postMapper.insert(post);
        PostContent postContent = PostContent.builder()
                .postId(post.getId())
                .content(dto.getContent())
                .build();
        postContentMapper.insert(postContent);

        searchService.saveDocument(post, postContent);
        return RestResponse.ok();
    }

    @Override
    public RestResponse<Void> delete(DeletePostDto dto) {
        postMapper.deleteById(dto.getPostId());
        return RestResponse.ok();
    }

    @Override
    public Post findById(Long id) {
        Post post = postMapper.selectById(id);
        if (Objects.isNull(post)) {
            throw new BusinessException(CommonResultStatus.RECORD_NOT_EXIST, "post does not exist");
        }
        return post;
    }

    @Override
    public List<Post> getTopPosts() {
        return postMapper.findTopPosts();
    }

    @Override
    public Page<Post> findPosts(int size, int page) {
        if (!limiter.tryAcquire()) {
            throw new BusinessException(CommonResultStatus.TOO_MANY_REQUEST, CommonResultStatus.TOO_MANY_REQUEST.getMsg());
        }
        Page<Post> postPage = PageDTO.of(page, size);
        return postMapper.selectPage(postPage, null);
    }

    @Override
    public void updateScore(Long postId, double score) {
        Post post = findById(postId);
        post.setScore(score);
        postMapper.updateById(post);
    }

    @Override
    public User getAuthor(Post post) {
        long id = post.getAuthorId();
        User author = userMapper.selectById(id);
        if (Objects.isNull(author)) {
            throw new BusinessException(CommonResultStatus.RECORD_NOT_EXIST, "author not found");
        }
        return author;
    }

    @Override
    public List<Comment> getComments(Post post) {
        return commentMapper.selectCommentsByPostId(post.getId());
    }

    @Override
    public PostContent getContent(Post post) {
        return postContentMapper.getContentByPost(post.getId());
    }


}

package com.forum.mantoi.sys.services.impl;

import com.forum.mantoi.common.pojo.request.DeletePostDto;
import com.forum.mantoi.common.pojo.request.PublishPostDto;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.Comment;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.PostContent;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.dao.mapper.PostContentMapper;
import com.forum.mantoi.sys.dao.mapper.PostMapper;
import com.forum.mantoi.sys.dao.mapper.UserMapper;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.services.PostService;
import com.forum.mantoi.sys.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


/**
 * @author DELL
 */
@Service
@Slf4j
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;

    private final UserMapper userMapper;

    private final PostContentMapper postContentMapper;

    @Override
    public RestResponse<Void> publish(PublishPostDto dto) {
        Post post = Post.builder()
                .authorId(dto.getAuthor().getId())
                .title(dto.getTitle())
                .likes(0)
                .score(0D)
                .build();
        postMapper.insert(post);
        PostContent postContent = PostContent.builder()
                .postId(post.getId())
                .content(dto.getContent())
                .build();
        postContentMapper.insert(postContent);

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
        return null;
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void updateScore(Long postId, double score) {

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
    }
}

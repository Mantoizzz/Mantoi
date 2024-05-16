package com.forum.mantoi.sys.services.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.forum.mantoi.common.annotation.AccessInterceptor;
import com.forum.mantoi.common.pojo.dto.request.DeletePostDto;
import com.forum.mantoi.common.pojo.dto.request.PostInformationDto;
import com.forum.mantoi.common.pojo.dto.request.PublishPostDto;
import com.forum.mantoi.common.pojo.vo.CommentVO;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.common.response.RestResponse;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.PostContent;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.dao.mapper.CommentMapper;
import com.forum.mantoi.sys.dao.mapper.PostContentMapper;
import com.forum.mantoi.sys.dao.mapper.PostMapper;
import com.forum.mantoi.sys.dao.mapper.UserMapper;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.services.CommentService;
import com.forum.mantoi.sys.services.PostService;
import com.forum.mantoi.sys.services.SearchService;
import com.forum.mantoi.utils.CaffeineUtils;
import com.forum.mantoi.utils.NullValue;
import com.forum.mantoi.utils.RedisKeys;
import com.forum.mantoi.utils.RedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final CommentService commentService;

    /**
     * 1.首先根据dto创建Post实体然后插入到数据库里面
     * 2.然后把Post的Content单独拎出来插入到数据库里面
     * 3.针对搜索功能保存到倒排索引里面
     * 4.针对Rank功能，保存到SET里面，默认值设置为0
     *
     * @param dto DTO
     * @return RestResponse
     * @throws IOException exception
     */
    @Override
    public RestResponse<Void> publish(PublishPostDto dto) throws IOException {
        Post post = Post.builder()
                .authorId(dto.getAuthor().getId())
                .title(dto.getTitle())
                .shortContent(dto.getContent().substring(0, 25))
                .likes(0)
                .score(0D)
                .deleted(false)
                .build();
        postMapper.insert(post);

        PostContent postContent = PostContent.builder()
                .postId(post.getId())
                .content(dto.getContent())
                .build();
        postContentMapper.insert(postContent);

        searchService.saveDocument(post, postContent);

        RedisUtils.zsetAdd(RedisKeys.getRankSet(), post.getId().toString(), 0);
        return RestResponse.ok();
    }

    /**
     * 1.找到被删除的Post
     * 2.把Post的deleted属性设置为true
     * 3.存到redis的set里面
     *
     * @param dto DTO
     * @return RestResponse
     */
    @Override
    public RestResponse<Void> delete(DeletePostDto dto) {
        Post post = postMapper.selectById(dto.getPostId());
        if (Objects.isNull(post)) {
            return RestResponse.fail(CommonResultStatus.RECORD_NOT_EXIST);
        }
        post.setDeleted(true);
        RedisUtils.sSet(RedisKeys.getDeletedPosts(), post.getId().toString());
        return RestResponse.ok();
    }

    //从DB中拿到数据后要缓存空值
    @Override
    public Post findById(Long id) {
        Post post = postMapper.selectById(id);
        if (Objects.isNull(post)) {
            RedisUtils.set(id.toString(), NullValue.getInstance(), 60L * 60 * 24);
            throw new BusinessException(CommonResultStatus.RECORD_NOT_EXIST, "post does not exist");
        }
        return post;
    }

    @Override
    public List<Post> getTopPosts() {
        return postMapper.findTopPosts();
    }

    @Override
    public Page<Post> findPosts(int size, int page, HttpServletRequest request) {
        return this.selectPosts(size, page, request.getRemoteAddr());
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
    public PostContent getContent(Post post) {
        return postContentMapper.getContentByPost(post.getId());
    }

    /**
     * 先走Caffeine，再走Redis，最后走MySQL
     *
     * @param postId id
     * @return dto
     */
    @Override
    public PostInformationDto getPostFromDatabase(long postId) {
        Post post = this.findById(postId);
        if (Objects.isNull(post)) {
            return null;
        }
        PostContent postContent = this.getContent(post);
        List<CommentVO> postComments = commentService.getPostComments(post);
        User author = this.getAuthor(post);
        PostInformationDto postInformationDto = new PostInformationDto();
        postInformationDto.setPostContent(postContent);
        postInformationDto.setPost(post);
        postInformationDto.setCommentList(postComments);
        postInformationDto.setAuthor(author);
        return postInformationDto;
    }

    /**
     * 先走Caffeine，再走Redis，最后走MySQL
     *
     * @param postId id
     * @return dto
     */
    @Override
    public PostInformationDto getPostDetail(long postId) {
        String id = String.valueOf(postId);
        Object o = CaffeineUtils.get(id);
        if (Objects.nonNull(o)) {
            return (PostInformationDto) o;
        }
        Object obj = RedisUtils.get(id);
        if (obj == NullValue.getInstance()) {
            return null;
        }
        if (Objects.nonNull(obj)) {
            return ((PostInformationDto) obj);
        }
        //走MySQL
        return this.getPostFromDatabase(postId);
    }

    @AccessInterceptor(key = "remoteAddr", blackListCount = 5, fallbackMethod = "fallback")
    private Page<Post> selectPosts(int size, int page, String remoteAddr) {
        Page<Post> postPage = PageDTO.of(page, size);
        return postMapper.selectPage(postPage, null);
    }


    private RestResponse<Void> fallback(int size, int page, String remoteAddr) {
        return RestResponse.fail(CommonResultStatus.TOO_MANY_REQUEST);
    }

}

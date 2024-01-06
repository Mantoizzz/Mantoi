package com.forum.mantoi.controller;

import com.forum.mantoi.common.CommonResultStatus;
import com.forum.mantoi.sys.entity.Comment;
import com.forum.mantoi.sys.entity.Post;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.services.CommentService;
import com.forum.mantoi.sys.services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Date;

/**
 * 评论Controller
 */
@Controller
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final PostService postService;

    /**
     * 这个方法针对评论Post
     *
     * @param postId  postId
     * @param content 内容
     * @return 重定向
     */
    @RequestMapping(path = "/add/{postId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("postId") long postId, String content) {
        User curUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postService.findById(postId).orElseThrow(()
                -> new BusinessException(CommonResultStatus.RECORD_NOT_EXIST, "post does not exist"));
        Comment comment = Comment.builder()
                .comments(new ArrayList<>())
                .likes(0)
                .publishTime(new Date())
                .author(curUser)
                .content(content)
                .parent(-1)
                .post(post)
                .build();
        commentService.publish(comment);
        post.getComments().add(comment);
        postService.save(post);
        //TODO:触发事件
        return "redirect:/post/" + postId;
    }

    /**
     * 这个方法针对回复评论
     *
     * @param postId    帖子ID
     * @param commentId 评论ID
     * @param content   内容
     * @return 重定向
     */
    @RequestMapping(path = "/add/{postId}/{comId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("postId") long postId, @PathVariable("comId") long commentId, String content) {
        User curUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postService.findById(postId).orElseThrow(()
                -> new BusinessException(CommonResultStatus.RECORD_NOT_EXIST, "post does not exist"));
        Comment parentComment = commentService.findById(commentId).orElseThrow(()
                -> new BusinessException(CommonResultStatus.RECORD_NOT_EXIST, "comment does not exist"));
        Comment newComment = Comment.builder()
                .post(post)
                .author(curUser)
                .comments(new ArrayList<>())
                .likes(0)
                .parent(commentId)
                .publishTime(new Date())
                .content(content)
                .build();
        commentService.addComment(parentComment, newComment);
        commentService.publish(newComment);
        //TODO:触发事件
        return "redirect:/post/" + postId;
    }


}

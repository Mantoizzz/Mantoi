package com.forum.mantoi.controller;

import com.forum.mantoi.common.CommonResultStatus;
import com.forum.mantoi.common.payload.PostRequest;
import com.forum.mantoi.sys.entity.Comment;
import com.forum.mantoi.sys.entity.Post;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.exception.UserException;
import com.forum.mantoi.sys.model.Entity;
import com.forum.mantoi.sys.services.CommentService;
import com.forum.mantoi.sys.services.LikeService;
import com.forum.mantoi.sys.services.PostService;
import com.forum.mantoi.sys.services.UserService;
import com.forum.mantoi.utils.CommunityUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@AllArgsConstructor
@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    private final LikeService likeService;

    private final CommentService commentService;

    private final int PAGE_SIZE = 10;

    /*
    当前登录的用户
     */
    @ModelAttribute("curUser")
    public String user() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            return authentication.getName();
        } else {
            return "Anonymous User";
        }
    }

    /*
    在发布一篇帖子前要判断当前用户是否为Anonymous User,否则不提供发布帖子功能
     */
    @ModelAttribute("postRequest")
    public PostRequest publishPost() throws UserException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            throw new UserException(CommonResultStatus.UNAUTHORIZED, "请登录再使用该功能");
        }
        return new PostRequest();
    }

    /*
    帖子列表
     */
    @ModelAttribute("postList")
    public Page<Post> pages(@PageableDefault(size = 10) Pageable pageable) {
        return postService.findAll(pageable);
    }

    @GetMapping
    public String post() {
        return "post";
    }

    @PostMapping("/add")
    @ResponseBody
    public String addPost(@ModelAttribute(name = "postRequest") PostRequest postRequest) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            throw new UserException(CommonResultStatus.UNAUTHORIZED, "请登录后再使用该功能");
        }
        Post post = Post.builder()
                .author(user)
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .publishTime(new Date())
                .comments(new ArrayList<>())
                .likes(0)
                .build();
        postService.publish(post);
        return CommunityUtil.getJsonString(CommonResultStatus.OK);
    }

    /**
     * 访问论坛的处理方法
     *
     * @param postId  postId
     * @param model   model
     * @param curPage 目前的page数
     * @return URL
     */
    @RequestMapping(path = "/detail/{postId}/{page}", method = RequestMethod.GET)
    public String getPost(@PathVariable("postId") long postId, Model model, @PathVariable("page") int curPage) {

        Post post = postService.findById(postId)
                .orElseThrow(() -> new BusinessException(CommonResultStatus.RECORD_NOT_EXIST, "post does not exist"));
        model.addAttribute("post", post);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User curUser = authentication == null ? null : (User) authentication.getPrincipal();
        model.addAttribute("user", curUser);
        User author = post.getAuthor();
        model.addAttribute("author", author);
        long likeCount = likeService.viewLikes(Entity.POST, postId);
        model.addAttribute("likes", likeCount);
        boolean isLiked = curUser != null && likeService.isLiked(Entity.POST, postId, curUser.getId());
        model.addAttribute("isLiked", isLiked);
        List<Comment> comments = commentService.findCommentsById(Entity.POST, postId);
        int start = Math.max(0, curPage * PAGE_SIZE);
        int end = Math.min(start + PAGE_SIZE, comments.size());
        comments = comments.subList(start, end);

        List<Map<String, Object>> commentVOList = new ArrayList<>();
        if (!comments.isEmpty()) {
            for (Comment comment : comments) {
                Map<String, Object> map = new HashMap<>();
                map.put("comment", comment);
                map.put("author", comment.getAuthor());
                map.put("likes", comment.getLikes());
                boolean commentLiked = curUser != null && likeService.isLiked(Entity.COMMENT, comment.getId(), curUser.getId());
                map.put("isLiked", commentLiked);
                List<Comment> replyList = commentService.findCommentsById(Entity.COMMENT, comment.getId());
                List<Map<String, Object>> replyVOList = new ArrayList<>();
                if (!replyList.isEmpty()) {
                    for (Comment reply : replyList) {
                        Map<String, Object> vo = new HashMap<>();
                        vo.put("reply", reply);
                        vo.put("replyAuthor", reply.getAuthor());
                        Comment parentComment = commentService.findParent(reply);
                        vo.put("parent", parentComment);

                        long likeCnt = likeService.viewLikes(Entity.COMMENT, reply.getId());
                        vo.put("likeCnt", likeCnt);
                        boolean likeStatus = curUser != null && likeService.isLiked(Entity.COMMENT, reply.getId(), curUser.getId());
                        vo.put("likeStatus", likeStatus);

                        replyVOList.add(vo);
                    }
                }
                map.put("reply", replyVOList);

                int replyCount = comments.size();
                map.put("replyCount", replyCount);
                commentVOList.add(map);
            }
        }
        model.addAttribute("comments", commentVOList);
        return "/post/{postId}/detail";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String setDelete(long id) {
        postService.delete(id);

        //TODO:触发事件
        return CommonResultStatus.OK.toString();
    }


}

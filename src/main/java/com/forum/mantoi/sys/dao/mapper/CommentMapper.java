package com.forum.mantoi.sys.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.forum.mantoi.sys.dao.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author DELL
 */
public interface CommentMapper extends BaseMapper<Comment> {

    @Select(value = "select * from mantoi.t_comment where post_id=#{postId};")
    List<Comment> selectCommentsByPostId(@Param("postId") long postId);

    @Select(value = "select * from mantoi.t_comment where parent_id=#{commentId};")
    List<Comment> selectRepliesByCommentId(@Param("commentId") long commentId);

    @Select(value = "select * from mantoi.t_comment where author_id=#{userId};")
    List<Comment> selectCommentsByUserId(@Param("userId") long userId);


}

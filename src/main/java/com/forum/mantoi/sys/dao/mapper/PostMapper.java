package com.forum.mantoi.sys.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.forum.mantoi.common.constant.DataBaseConstants;
import com.forum.mantoi.sys.dao.entity.Post;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author DELL
 * @see com.forum.mantoi.sys.dao.entity.PostContent
 */
public interface PostMapper extends BaseMapper<Post> {

    @Select(value = "select * from mantoi.t_post order by score desc limit 20;")
    public List<Post> findTopPosts();


    @Select(value = "select * from mantoi.t_post where author_id = #{userId};")
    public List<Post> findPostsByUserId(@Param("userId") long userId);
}

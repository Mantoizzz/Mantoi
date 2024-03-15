package com.forum.mantoi.sys.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.forum.mantoi.sys.dao.entity.PostContent;
import org.apache.ibatis.annotations.Select;

/**
 * @author DELL
 * @see com.forum.mantoi.sys.dao.entity.Post
 */
public interface PostContentMapper extends BaseMapper<PostContent> {

    @Select("select * from mantoi.t_post_content where post_id = #{id} limit 1;")
    PostContent getContentByPost(long id);

}

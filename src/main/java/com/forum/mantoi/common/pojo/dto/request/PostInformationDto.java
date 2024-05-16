package com.forum.mantoi.common.pojo.dto.request;

import com.forum.mantoi.common.pojo.vo.CommentVO;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.PostContent;
import com.forum.mantoi.sys.dao.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author DELL
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostInformationDto implements Serializable {

    private Post post;

    private User author;

    private PostContent postContent;

    private List<CommentVO> commentList;

}

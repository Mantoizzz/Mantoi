package com.forum.mantoi.common.pojo.response;

import com.forum.mantoi.sys.dao.entity.Post;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author DELL
 */
@Data
@Builder
public class UserProfileDto {

    String avatar;

    String username;

    String introduction;

    String gender;

    long likes;

    long subscribers;

    long fans;

    List<?> publish;

}

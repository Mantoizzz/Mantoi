package com.forum.mantoi.common.pojo.dto.request;

import com.forum.mantoi.sys.dao.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author DELL
 */

@Data
public class PublishPostDto {

    public String content;

    public User author;

    public Date publishTime;

    public String title;

}

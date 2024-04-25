package com.forum.mantoi.common.pojo.dto.request;

import com.forum.mantoi.sys.dao.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author DELL
 */

@Data
public class PublishPostDto implements Serializable {

    private String content;

    private String shortContent;

    private User author;

    private Date publishTime;

    private String title;

}

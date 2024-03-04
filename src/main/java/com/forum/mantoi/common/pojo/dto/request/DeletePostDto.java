package com.forum.mantoi.common.pojo.dto.request;

import lombok.Data;

/**
 * @author DELL
 */
@Data
public class DeletePostDto {

    Long postId;

    public DeletePostDto() {

    }

    public DeletePostDto(long id) {
        this.postId = id;
    }

}

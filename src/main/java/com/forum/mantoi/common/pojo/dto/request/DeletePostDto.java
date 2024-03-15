package com.forum.mantoi.common.pojo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author DELL
 */
@Data
@AllArgsConstructor
public class DeletePostDto {

    Long postId;

    Long userId;

    public DeletePostDto(long id) {
        this.postId = id;
    }

}

package com.forum.mantoi.common.pojo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author DELL
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeletePostDto implements Serializable {

    Long postId;

    Long userId;

}

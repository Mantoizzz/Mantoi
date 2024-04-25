package com.forum.mantoi.common.pojo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author DELL
 */
@Data
@Builder
public class RegisterResponseDto implements Serializable {

    private String token;

}

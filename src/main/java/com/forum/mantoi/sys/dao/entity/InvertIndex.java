package com.forum.mantoi.sys.dao.entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author DELL
 */
@Document
@Data
public class InvertIndex implements Serializable {

    @Id
    private String keyword;

    private long[] documents;

}

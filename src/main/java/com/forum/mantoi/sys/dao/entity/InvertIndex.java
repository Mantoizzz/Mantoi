package com.forum.mantoi.sys.dao.entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class InvertIndex {

    @Id
    private String keyword;

    private long[] documents;

}

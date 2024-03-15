package com.forum.mantoi.sys.dao.entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class InvertIndex {

    @Id
    private String keyword;

    private List<Long> documents;

}

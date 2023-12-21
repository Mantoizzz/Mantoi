package com.forum.mantoi.sys.elasticsearch;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Document(indexName = "es_post")
@Data
@Entity
public class EsPost {

    @Id
    Long id;

    @Field(name = "content", type = FieldType.Text)
    String content;

    @Field(name = "title", type = FieldType.Text)
    String title;

    @Field(name = "time", type = FieldType.Date)
    Date createTime;

}

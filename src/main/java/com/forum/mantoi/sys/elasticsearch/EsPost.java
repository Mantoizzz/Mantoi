package com.forum.mantoi.sys.elasticsearch;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;

@Document(indexName = "es_post")
@Data
@Entity
@Deprecated
public class EsPost {

    @Id
    Long id;

    @Field(name = "content", type = FieldType.Text)
    @HighlightField(name = "content", parameters = @HighlightParameters(preTags = "<em>", postTags = "</em>"))
    String content;

    @Field(name = "title", type = FieldType.Text)
    @HighlightField(name = "title", parameters = @HighlightParameters(preTags = "<em>", postTags = "</em>"))
    String title;

    @Field(name = "time", type = FieldType.Date)
    Date createTime;

    @Field(name = "like", type = FieldType.Integer)
    Integer likes;

    @Field(name = "author", type = FieldType.Long)
    Long authorId;

}

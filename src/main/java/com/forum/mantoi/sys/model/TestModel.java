package com.forum.mantoi.sys.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "test_model")
public class TestModel {

    @Field(name = "content", type = FieldType.Text)
    String content;

    @Field(name = "title", type = FieldType.Text)
    String title;

    @Field(name = "num", type = FieldType.Text)
    Integer num;

    @Field(name = "date", type = FieldType.Date)
    Date date;


}

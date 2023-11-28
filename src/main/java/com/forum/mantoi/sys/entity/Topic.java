package com.forum.mantoi.sys.entity;


import jakarta.persistence.*;
import org.springframework.data.elasticsearch.annotations.Document;

@Entity
@Table(name = "t_topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;


}

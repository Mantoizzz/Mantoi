package com.forum.mantoi.sys.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "t_post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "title")
    private String title;

    @ManyToOne(targetEntity = User.class)
    @JoinTable(name = "t_user_post", joinColumns = {@JoinColumn(name = "pid")}, inverseJoinColumns = {@JoinColumn(name = "uid")})
    private User author;

    @OneToMany(targetEntity = Comment.class)
    @JoinTable(name = "t_post_com", joinColumns = {@JoinColumn(name = "pid")}, inverseJoinColumns = {@JoinColumn(name = "cid")})
    private List<Comment> comments;

    @Column(name = "likes")
    private Integer likes;

    @Column(name = "publish")
    private Date publishTime;

    @Column(name = "score")
    private Double score;

    /*
      浏览数准备用redis进行存储
     */
    //TODO 存储浏览数

}

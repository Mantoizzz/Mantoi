package com.forum.mantoi.sys.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "t_post")
public class Posting {

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

    @OneToMany(targetEntity = CommentPost.class)
    @JoinTable(name = "t_post_compost", joinColumns = {@JoinColumn(name = "pid")}, inverseJoinColumns = {@JoinColumn(name = "cpid")})
    private List<CommentPost> commentPosts;

    @Column(name = "likes")
    private Integer likes;

    /*
      浏览数准备用redis进行存储
     */

}

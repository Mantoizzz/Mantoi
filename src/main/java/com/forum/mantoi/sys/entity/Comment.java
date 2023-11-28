package com.forum.mantoi.sys.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "t_comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = User.class)
    @JoinTable(name = "t_user_comment", joinColumns = {@JoinColumn(name = "cid")}, inverseJoinColumns = {@JoinColumn(name = "uid")})
    private User author;

    @Column(name = "content")
    private String content;

    @OneToMany(targetEntity = Comment.class)
    @JoinTable(name = "t_com_com", joinColumns = {@JoinColumn(name = "cid1")}, inverseJoinColumns = {@JoinColumn(name = "cid2")})
    private List<Comment> comments;

    @Column(name = "likes")
    private Integer likes;

    @ManyToOne(targetEntity = CommentPost.class)
    @JoinTable(name = "t_comp_com", joinColumns = {@JoinColumn(name = "cid")}, inverseJoinColumns = {@JoinColumn(name = "cpid")})
    private CommentPost commentPost;

    @Column(name = "publish")
    private Date publishTime;

}

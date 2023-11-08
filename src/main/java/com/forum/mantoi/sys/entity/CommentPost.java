package com.forum.mantoi.sys.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "t_comment_post")
public class CommentPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne(targetEntity = User.class)
    @JoinTable(name = "t_user_compost", joinColumns = {@JoinColumn(name = "cid")}, inverseJoinColumns = {@JoinColumn(name = "cpid")})
    private User author;

    @Column(name = "likes")
    private Integer likes;

    @ManyToOne(targetEntity = Posting.class)
    @JoinTable(name = "t_post_compost", joinColumns = {@JoinColumn(name = "cpid")}, inverseJoinColumns = {@JoinColumn(name = "pid")})
    private Posting posting;

    @OneToMany(targetEntity = Comment.class)
    @JoinTable(name = "t_comp_com", joinColumns = {@JoinColumn(name = "cpid")}, inverseJoinColumns = {@JoinColumn(name = "cid")})
    private List<Comment> comments;
}

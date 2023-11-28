package com.forum.mantoi.sys.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor

@Table(name = "t_commentpost")
public class CommentPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne(targetEntity = User.class)
    @JoinTable(name = "t_user_compost", joinColumns = {@JoinColumn(name = "cpid")}, inverseJoinColumns = {@JoinColumn(name = "uid")})
    private User author;

    @Column(name = "likes")
    private Integer likes;

    @ManyToOne(targetEntity = Post.class)
    @JoinTable(name = "t_post_compost", joinColumns = {@JoinColumn(name = "cpid")}, inverseJoinColumns = {@JoinColumn(name = "pid")})
    private Post post;

    @OneToMany(targetEntity = Comment.class)
    @JoinTable(name = "t_comp_com", joinColumns = {@JoinColumn(name = "cpid")}, inverseJoinColumns = {@JoinColumn(name = "cid")})
    private List<Comment> comments;

    @Column(name = "publish")
    private Date publishTime;
}

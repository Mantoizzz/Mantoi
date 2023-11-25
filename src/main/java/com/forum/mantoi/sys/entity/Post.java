package com.forum.mantoi.sys.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(targetEntity = CommentPost.class)
    @JoinTable(name = "t_post_compost", joinColumns = {@JoinColumn(name = "pid")}, inverseJoinColumns = {@JoinColumn(name = "cpid")})
    private List<CommentPost> commentPosts;

    @Column(name = "likes")
    private Integer likes;

    @Column(name = "publish")
    private Date publishTime;

    /*
      浏览数准备用redis进行存储
     */

}

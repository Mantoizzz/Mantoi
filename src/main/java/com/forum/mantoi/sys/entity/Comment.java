package com.forum.mantoi.sys.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor

@Table(name = "t_comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne(targetEntity = User.class)
    @JoinTable(name = "t_user_com", joinColumns = {@JoinColumn(name = "cid")}, inverseJoinColumns = {@JoinColumn(name = "uid")})
    private User author;

    @Column(name = "likes")
    private Integer likes;

    @ManyToOne(targetEntity = Post.class)
    @JoinTable(name = "t_post_com", joinColumns = {@JoinColumn(name = "cid")}, inverseJoinColumns = {@JoinColumn(name = "pid")})
    private Post post;

    @OneToMany(targetEntity = Comment.class)
    @JoinTable(name = "t_com_com", joinColumns = {@JoinColumn(name = "cid1")}, inverseJoinColumns = {@JoinColumn(name = "cid2")})
    private List<Comment> comments;

    @Column(name = "parent")
    private long parent;

    @Column(name = "publish")
    private Date publishTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

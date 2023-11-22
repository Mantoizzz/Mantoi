package com.forum.mantoi.sys.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "create")
    private Date createTime;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "role")
    private String role;

    @ManyToMany(targetEntity = User.class)
    @JoinTable(name = "t_user_sub", joinColumns = {@JoinColumn(name = "uid")}, inverseJoinColumns = {@JoinColumn(name = "sid")})
    private List<User> subscribers;

    @ManyToMany(targetEntity = User.class)
    @JoinTable(name = "t_user_follow", joinColumns = {@JoinColumn(name = "uid")}, inverseJoinColumns = {@JoinColumn(name = "fid")})
    private List<User> followers;

    @OneToMany(targetEntity = Post.class)
    @JoinTable(name = "t_user_post", joinColumns = {@JoinColumn(name = "uid")}, inverseJoinColumns = {@JoinColumn(name = "pid")})
    private List<Post> posts;

    private enum Gender {
        MALE,
        FEMALE
    }
}

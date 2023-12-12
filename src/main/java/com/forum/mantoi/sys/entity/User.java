package com.forum.mantoi.sys.entity;

import com.forum.mantoi.sys.model.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
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

    @Column(name = "intro")
    private String introduction;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "avatar")
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinTable(name = "t_user_sub", joinColumns = {@JoinColumn(name = "uid")}, inverseJoinColumns = {@JoinColumn(name = "sid")})
    private List<User> subscribers;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinTable(name = "t_user_follow", joinColumns = {@JoinColumn(name = "uid")}, inverseJoinColumns = {@JoinColumn(name = "fid")})
    private List<User> followers;

    @OneToMany(targetEntity = Post.class)
    @JoinTable(name = "t_user_post", joinColumns = {@JoinColumn(name = "uid")}, inverseJoinColumns = {@JoinColumn(name = "pid")})
    private List<Post> posts;

    @Override
    public String toString() {
        return this.username;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User target)) {
            return false;
        }
        return this.email.equals(target.getEmail());
    }

}

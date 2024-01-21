package com.forum.mantoi.sys.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;


/**
 * @author DELL
 */
@Entity
@Data
@RequiredArgsConstructor
@Table(name = "t_word")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @OneToMany
    @JoinTable(name = "t_word_post", joinColumns = {@JoinColumn(name = "wid")}, inverseJoinColumns = {@JoinColumn(name = "pid")})
    private List<Post> posts;


}

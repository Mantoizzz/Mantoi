package com.forum.mantoi.sys.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "t_message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from")
    private Long fromId;

    @Column(name = "to")
    private Long toId;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private int status;

    @Column(name = "create")
    private Date createTime;
}

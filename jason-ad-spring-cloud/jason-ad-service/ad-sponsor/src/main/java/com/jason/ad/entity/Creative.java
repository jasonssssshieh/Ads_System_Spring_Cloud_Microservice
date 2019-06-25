package com.jason.ad.entity;

/*
* 创意, 就是我们最后返回给用户的数据, 也就是创意*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad_creative")
public class Creative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "type", nullable = false)
    //创意的主类型: 比如是一个图片?文本?视频?音频?等等
    private Integer type;

    /** 物料类型, 比如图片可以是jpeg, png等等*/
    @Basic
    @Column(name = "material_type", nullable = false)
    private Integer materialType;

    @Basic
    @Column(name = "height", nullable = false)
    private Integer height;

    @Basic
    @Column(name = "width", nullable = false)
    private Integer width;

    /*
    * 物料大小*/
    @Basic
    @Column(name = "size", nullable = false)
    private Long size;

    /*
    持续时长, 只有视频类的才不为0*/
    @Basic
    @Column(name = "duration", nullable = false)
    private Integer duration;

    /*
    * 审核状态*/
    @Basic
    @Column(name = "audit_status", nullable = false)
    private Integer auditStatus;

    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Basic
    @Column(name = "url", nullable = false)
    private String url;


    @Basic
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Basic
    @Column(name = "update_time", nullable = false)
    private Date updateTime;
}

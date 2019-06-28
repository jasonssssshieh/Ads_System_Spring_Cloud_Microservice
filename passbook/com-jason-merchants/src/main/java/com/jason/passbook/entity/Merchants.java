package com.jason.passbook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/*
商户对象模型
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "merchants")
public class Merchants {

    /*商户id, 主键*/
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Integer id;

    /*商户名称, 全局位移*/
    @Basic
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    /*商户logo*/
    @Basic
    @Column(name = "logo_url", nullable = false)
    private String logoUrl;

    @Basic
    @Column(name = "business_license_url", nullable = false)
    private String businessLicenseUrl;

    /*商户联系电话*/
    @Basic
    @Column(name = "phone", nullable = false)
    private String phone;

    @Basic
    @Column(name = "address", nullable = false)
    private String address;

    @Basic
    @Column(name = "is_audit", nullable = false)
    private Boolean isAudit = false;
}
package com.jason.passbook.dao;

import com.jason.passbook.entity.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;

//MerchantsDao接口
//<Merchants, Integer> Merchants代表了我们要对Merchants这张表进行操作, Integer代表了主键类型
public interface MerchantsDao extends JpaRepository <Merchants, Integer> {

    /**
     * 根据id获取商户对象
     * @param id 商户id
     * @return {@link Merchants}
     */
    Merchants findById(Integer id);

    /**
     * 根据商户名称获取商户对象
     * @param name 商户名称
     * @return {@link Merchants}
     */
    Merchants findByName(String name);
}

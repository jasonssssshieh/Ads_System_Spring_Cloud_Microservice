package com.jason.ad.dao;

import com.jason.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdUserRepository extends JpaRepository<AdUser, Long> {
    /*
    根据用户名查找用户记录
     */
    AdUser findByUsername(String username);//因为username是unique的 所以就是会只返回一个Aduser
}

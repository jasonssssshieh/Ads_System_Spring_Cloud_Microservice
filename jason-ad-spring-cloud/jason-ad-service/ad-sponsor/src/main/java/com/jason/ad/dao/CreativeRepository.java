package com.jason.ad.dao;

import com.jason.ad.entity.Creative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreativeRepository extends JpaRepository <Creative, Long>{

}
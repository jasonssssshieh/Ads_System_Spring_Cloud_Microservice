package com.jason.ad.dao.unit_condition;

import com.jason.ad.entity.unit_condition.AdUnitDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdUnitDistrictRepository extends JpaRepository<AdUnitDistrict, Long> {
}

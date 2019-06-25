package com.jason.ad.index.district;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitDistrictObject {
    private String state;
    private String city;
    private Long unitId;

    //<String, Set<Long>>
    //<state-city>=>作为一个key传进来
}

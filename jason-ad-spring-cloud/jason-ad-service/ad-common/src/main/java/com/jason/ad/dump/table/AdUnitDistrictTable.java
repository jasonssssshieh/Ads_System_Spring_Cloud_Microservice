package com.jason.ad.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AdUnitDistrictTable {
    private Long unitId;
    private String state;
    private String city;
}

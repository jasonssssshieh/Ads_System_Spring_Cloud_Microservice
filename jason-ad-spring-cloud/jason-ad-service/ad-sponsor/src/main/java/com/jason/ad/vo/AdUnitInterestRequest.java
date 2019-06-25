package com.jason.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitInterestRequest {

    private List<UnitInterest> unitIts;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitInterest{
        private Long unitId;
        private String itTag;
    }
}

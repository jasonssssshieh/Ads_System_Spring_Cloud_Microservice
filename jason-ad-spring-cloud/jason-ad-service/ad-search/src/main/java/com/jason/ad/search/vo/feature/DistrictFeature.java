package com.jason.ad.search.vo.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictFeature {

    private List<StateAndCity> districts;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StateAndCity{
        private String state;
        private String city;
    }
}

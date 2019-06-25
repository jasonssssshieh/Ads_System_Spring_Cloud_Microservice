package com.jason.ad.index.creativeunit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreativeUnitObject {
    private Long adId;//creative的id
    private Long unitId;//廣告單元的id

    //adId-unitId 結合在一起能唯一確定. 作為一個string的key <adId-unitId>
}


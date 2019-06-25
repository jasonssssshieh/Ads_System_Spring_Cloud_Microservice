package com.jason.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Geo {
    private Float latitude;
    private Float longitude;

    private String city;
    private String state;
}

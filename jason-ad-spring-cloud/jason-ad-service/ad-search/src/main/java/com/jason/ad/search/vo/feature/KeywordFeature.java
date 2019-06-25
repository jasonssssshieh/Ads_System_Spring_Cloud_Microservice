package com.jason.ad.search.vo.feature;

//关键词的匹配信息
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeywordFeature {
    private List<String> keywords;
}

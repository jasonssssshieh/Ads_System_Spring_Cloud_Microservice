package com.jason.ad.search;


import com.jason.ad.search.vo.SearchRequest;
import com.jason.ad.search.vo.SearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//用于广告检索请求

public interface ISearch {

    SearchResponse fetchAds(SearchRequest request);

}

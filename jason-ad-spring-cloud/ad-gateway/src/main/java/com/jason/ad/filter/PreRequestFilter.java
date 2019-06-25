package com.jason.ad.filter;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * 假如我们要实现一个打印进来和出去的时候interval的过滤器,那么就需要preRequest以及postRequest
 * 才能处理我们的请求
 */
//把过滤器被发现,才能注册到容器中
@Slf4j
@Component
public class PreRequestFilter extends ZuulFilter{
    @Override
    //定义filter类型
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    //定义filter的执行顺序 越小越先被执行
    public int filterOrder() {
        return 0;
    }

    @Override
    //表示是否需要执行这个filter 默认是false
    public boolean shouldFilter() {
        return true;
    }

    @Override
    //filter需要执行的具体操作
    public Object run() throws ZuulException {

        //这个requestcontext会在过滤器整个执行期间 一直传递下去
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("startTime", System.currentTimeMillis());//他是一个key value pair结构的
        return null;
    }
}

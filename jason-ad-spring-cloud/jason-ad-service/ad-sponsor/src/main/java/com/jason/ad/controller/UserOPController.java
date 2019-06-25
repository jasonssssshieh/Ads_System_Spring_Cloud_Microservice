package com.jason.ad.controller;

import com.alibaba.fastjson.JSON;
import com.jason.ad.exception.AdException;
import com.jason.ad.service.IUserService;
import com.jason.ad.vo.CreateUserRequest;
import com.jason.ad.vo.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController//直接返回json类型的数
public class UserOPController {

    private final IUserService userService;
    @Autowired
    public UserOPController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create/user")
    /*因为是我们要创建东西
    另外, 由于我们在application.yml里面配置了 context path 所以这里的前缀隐含了 /ad-sponsor
    而不是直接是/create/user
    */
    //这里@RequestBody是进行反序列化
    public CreateUserResponse createUserResponse(
            @RequestBody CreateUserRequest request) throws AdException{
        log.info("ad-sponsor: createUser -> {}",
                JSON.toJSONString(request));//写入日志
        return userService.createUser(request);
    }

}

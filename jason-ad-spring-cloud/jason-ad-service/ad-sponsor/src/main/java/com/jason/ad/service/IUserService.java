package com.jason.ad.service;

import com.jason.ad.exception.AdException;
import com.jason.ad.vo.CreateUserRequest;
import com.jason.ad.vo.CreateUserResponse;

public interface IUserService {
    //create user 创建user
    /*
     * <h2>创建用户</h2>*/
    CreateUserResponse createUser (CreateUserRequest createUserRequest) throws AdException;
}

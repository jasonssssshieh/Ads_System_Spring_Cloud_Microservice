package com.jason.ad.service.impl;

import com.jason.ad.constant.Constants;
import com.jason.ad.dao.AdUserRepository;
import com.jason.ad.entity.AdUser;
import com.jason.ad.exception.AdException;
import com.jason.ad.service.IUserService;
import com.jason.ad.utils.CommonUtils;
import com.jason.ad.vo.CreateUserRequest;
import com.jason.ad.vo.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j//给我们添加日志的属性
@Service//标记为java spring中的一个bean
public class UserServiceImpl implements IUserService{
    //因为我们创建用户需要用到创建用户的DAO接口,所以这里需要注入
    private final AdUserRepository userRepository;

    @Autowired
    public UserServiceImpl(AdUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    //因为我们要往数据库里去write, 所以我们这里需要一个注解transactional, 开启事务
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request)
            throws AdException {
        //判断传进来的参数是否正确
        if(!request.validate()){
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        //判断是否当前系统中有同名用户了
        AdUser oldUser = userRepository.
                findByUsername(request.getUsername());

        if(oldUser != null){
            throw new AdException(Constants.ErrorMsg.SAME_NAME_ERROR);
        }

        //创建AdUser需要两个参数 username和token
        /*
        token我们需要去对username做md5
         */
        AdUser newUser = userRepository.save(new AdUser(
                request.getUsername(), CommonUtils.md5(request.getUsername())

        ));
        return new CreateUserResponse(
                newUser.getId(), newUser.getUsername(), newUser.getToken(),
                newUser.getCreateTime(), newUser.getUpdateTime()
        );
    }
}

package org.github.caishijun.eshop.product.ha.service.impl;

import org.github.caishijun.eshop.product.ha.mapper.UserInfoMapper;
import org.github.caishijun.eshop.product.ha.model.UserInfo;
import org.github.caishijun.eshop.product.ha.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    public UserInfo selectById(Integer id){
        return userInfoMapper.selectById(id);
    }

}

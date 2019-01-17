package org.github.caishijun.eshop.cache.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.github.caishijun.eshop.cache.dao.RedisDAO;
import org.github.caishijun.eshop.cache.mapper.UserInfoMapper;
import org.github.caishijun.eshop.cache.model.UserInfo;
import org.github.caishijun.eshop.cache.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisDAO redisDAO;

    public UserInfo selectById(Integer id){
        return userInfoMapper.selectById(id);
    }

    @Override
    public UserInfo getCachedUserInfo() {
        redisDAO.set("cached_user", "{\"id\": 25 , \"name\": \"zhangsan\"}") ;
        String json = redisDAO.get("cached_user");
        JSONObject jsonObject = JSONObject.parseObject(json);

        UserInfo user = new UserInfo();
        user.setId(jsonObject.getInteger("id").toString());
        user.setUserName(jsonObject.getString("name"));

        return user;
    }
}
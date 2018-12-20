package org.github.caishijun.eshop.cache.service;

import org.github.caishijun.eshop.cache.model.UserInfo;

public interface UserInfoService {

    UserInfo selectById(Integer id);

    UserInfo getCachedUserInfo();

}

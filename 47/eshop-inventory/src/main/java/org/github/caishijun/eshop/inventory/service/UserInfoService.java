package org.github.caishijun.eshop.inventory.service;

import org.github.caishijun.eshop.inventory.model.UserInfo;


public interface UserInfoService {

    UserInfo selectById(Integer id);

    UserInfo getCachedUserInfo();

}

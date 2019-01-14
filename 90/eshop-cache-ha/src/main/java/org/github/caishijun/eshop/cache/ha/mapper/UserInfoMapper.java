package org.github.caishijun.eshop.cache.ha.mapper;

import org.apache.ibatis.annotations.Param;
import org.github.caishijun.eshop.cache.ha.model.UserInfo;


public interface UserInfoMapper {

    UserInfo selectById(@Param("id") Integer id);
}

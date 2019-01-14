package org.github.caishijun.eshop.cache.mapper;

import org.apache.ibatis.annotations.Param;
import org.github.caishijun.eshop.cache.model.UserInfo;


public interface UserInfoMapper {

    UserInfo selectById(@Param("id") Integer id);
}

package org.github.caishijun.eshop.product.ha.mapper;

import org.apache.ibatis.annotations.Param;
import org.github.caishijun.eshop.product.ha.model.UserInfo;


public interface UserInfoMapper {

    UserInfo selectById(@Param("id") Integer id);
}

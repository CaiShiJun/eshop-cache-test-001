package org.github.caishijun.eshop.inventory.controller;

import org.github.caishijun.eshop.inventory.model.UserInfo;
import org.github.caishijun.eshop.inventory.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    // curl -X POST localhost:8080/userInfo/hello
    @PostMapping("/hello")
    public String hello(){
        return "hello SpringBoot";
    }

    // curl -X POST localhost:8080/userInfo/selectById -d "id=25"
    @PostMapping("/selectById")
    public UserInfo selectById(Integer id){
        return userInfoService.selectById(id);
    }

    // curl -X POST localhost:8080/userInfo/getCachedUserInfo
    @PostMapping("/getCachedUserInfo")
    public UserInfo getCachedUserInfo() {
        UserInfo user = userInfoService.getCachedUserInfo();
        return user;
    }
}
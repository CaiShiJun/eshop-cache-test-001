package org.github.caishijun.eshop.cache.spring;

import org.springframework.context.ApplicationContext;

/**
 * spring上下文
 *
 * 在初始化监听器中获取 SpringContext 保存进自定义的 SpringContext 包装类便于随时在各处代码去调用获取 SpringContext 。
 */
public class SpringContext {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.applicationContext = applicationContext;
    }

}

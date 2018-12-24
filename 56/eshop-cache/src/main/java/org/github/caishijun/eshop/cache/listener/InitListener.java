package org.github.caishijun.eshop.cache.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.github.caishijun.eshop.cache.kafka.KafkaConsumer;
import org.github.caishijun.eshop.cache.spring.SpringContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 系统初始化的监听器
 */
public class InitListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        // 在初始化监听器中获取 SpringContext 保存进自定义的 SpringContext 包装类便于随时在各处代码去调用获取 SpringContext 。
        ServletContext sc = sce.getServletContext();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
        SpringContext.setApplicationContext(context);

        new Thread(new KafkaConsumer("cache-message")).start();
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }

}
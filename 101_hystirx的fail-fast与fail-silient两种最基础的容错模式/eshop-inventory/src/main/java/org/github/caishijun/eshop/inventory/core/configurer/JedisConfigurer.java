package org.github.caishijun.eshop.inventory.core.configurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class JedisConfigurer {

    @Bean
    public JedisCluster JedisClusterFactory() {
        // 添加集群的服务节点Set集合
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        // 添加节点
        jedisClusterNodes.add(new HostAndPort("192.168.74.102", 7003));
        jedisClusterNodes.add(new HostAndPort("192.168.74.102", 7004));
        jedisClusterNodes.add(new HostAndPort("192.168.74.103", 7006));

        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        jedisPoolConfig.setMaxIdle(100);
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(500);
        //最小空闲连接数, 默认0
        jedisPoolConfig.setMinIdle(0);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(2000); // 设置2秒
        //对拿到的connection进行validateObject校验
        jedisPoolConfig.setTestOnBorrow(true);

        // soTimeout: 返回值的超时时间
        // maxAttempts：出现异常最大重试次数
        JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes, 2000,5000,5,"redis-pass",jedisPoolConfig);
        return jedisCluster;
    }

}

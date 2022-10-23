package cn.edu.hust.redisdl.spring.boot.autoconfigure;

import cn.edu.hust.redisdl.spring.boot.autoconfigure.properties.RedisSingletonProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于redisson的分布式锁自动配置类
 *
 * @Author wylu
 * @Date 2022/10/23 下午 03:14
 */
@Configuration
@EnableConfigurationProperties(RedisSingletonProperties.class)
public class RedisDistributedLockAutoConfigure {

    private final RedisSingletonProperties redisSingletonProperties;

    @Autowired
    public RedisDistributedLockAutoConfigure(RedisSingletonProperties redisSingletonProperties) {
        this.redisSingletonProperties = redisSingletonProperties;
    }

    /**
     * <a href="redisson单节点配置参考">https://github.com/redisson/redisson/wiki</a>
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "dl.redis.singleton", name = "address")
    public RedissonClient singletonClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(redisSingletonProperties.getAddress())
                .setPassword(redisSingletonProperties.getPassword())
                .setDatabase(redisSingletonProperties.getDatabase());

        return Redisson.create(config);
    }
}

[toc]

# 🔒基于Redisson实现的分布式锁Starter

## ♨️版本说明
| 依赖         | 版本            |
|------------|---------------|
| SpringBoot | 2.3.7.RELEASE |
| redisson   | 3.13.6        |

## 🍂类型
+ Redis单例版
+ Redis集群版
+ Redis哨兵版

## 📖使用说明
### 🐾引入依赖
#### 🪶maven
```xml
<dependencies>
    <dependency>
        <groupId>cn.edu.hust</groupId>
        <artifactId>redis-dl-spring-boot-starter</artifactId>
        <version>2.3.7-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>org.redisson</groupId>
        <artifactId>redisson-spring-boot-starter</artifactId>
        <version>3.13.6</version>
    </dependency>
</dependencies>
```

#### 🐘gradle
```groovy
dependencies {
    implementation(
        'cn.edu.hust:redis-dl-spring-boot-starter:2.3.7-SNAPSHOT',
        'org.redisson:redisson-spring-boot-starter:3.13.6'
    )
}
```

### 🌹单例版
#### 📝yml配置
```yaml
dl:
  redis:
    singleton:
      address: redis://localhost:6379
      password: root123
      database: 10
```

#### 🎉示例
```java
@SpringBootTest
public class RedissonDLTests {
    @Autowired
    private RedissonClient redissonClient;

    @Test
    void test() {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                RLock lock = redissonClient.getLock("LOCK_" + 1);
                try {
                    if (lock.tryLock(30, 10, TimeUnit.SECONDS)) {
                        System.out.println("线程" + Thread.currentThread().getName() + "获取锁");
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            // ignore
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (lock != null && lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }, String.valueOf(i + 1)).start();
        }
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
```
package com.zhoujian.redisdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

@SpringBootTest
class RedisdemoApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
        BoundHashOperations<String, String, Object> uamp_user = stringRedisTemplate.boundHashOps("Uamp_User");
        System.out.println(uamp_user.getKey());
        uamp_user.values().forEach(System.out::println);
        Set<String> keys = uamp_user.keys();

        // 获取值
        Object o = uamp_user.get("a005091b-5667-4464-9e7e-24ad2bc49e5b");
    }

}

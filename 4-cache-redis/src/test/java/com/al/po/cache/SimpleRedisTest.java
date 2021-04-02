package com.al.po.cache;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SimpleRedisTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    public void simpleTest() {
        redisTemplate.opsForValue().set("test", "a");
        String value = String.valueOf(redisTemplate.opsForValue().get("test"));
        log.info("===========simpleTest===========");
        Assert.assertEquals(value, "a");
    }

}

package com.al.po.cache;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 秒杀系统模拟
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SeckillRedisTest {

    DefaultRedisScript script;

    /**
     * 脚本用于判断库存并锁定，使两个动作为原子操作
     */
    @Before
    public void init() {
        script = new DefaultRedisScript();
        script.setScriptSource(new ResourceScriptSource(
                new ClassPathResource("seckill.lua")
        ));
        script.setResultType(Integer.class);
    }

    @Autowired
    StringRedisTemplate redisTemplate;

    static final String goodsId = "seckill:goods:%s";

    String getKey(String id) {
        return String.format(goodsId, id);
    }

    public void prepare(String id, int total) {
        String key = getKey(id);
        if (redisTemplate.hasKey(key)) {
            return;
        }
        Map<String, String> goods = new HashMap<>();
        goods.put("total", String.valueOf(total));
        goods.put("start", "0");
        goods.put("alloc", "0");
        redisTemplate.opsForHash().putAll(key, goods);
    }

    /**
     * 秒杀
     * @param id 商品ID
     * @param number 购买数量
     * @return 已被购买数量
     */
    public int secKill(String id, int number) {
        String key = getKey(id);
        Object alloc =  redisTemplate.execute(script, Arrays.asList(key), String.valueOf(number));
        return Integer.valueOf(alloc.toString());
    }

    @Test
    public void testSeckill() {
        String id = "116";
        prepare(id, 100);
        ExecutorService executor = Executors.newCachedThreadPool();
        int firstAlloc = secKill(id, 1);
        log.info("firstAlloc===============" + firstAlloc);
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                int alloc = secKill(id, 1);
                log.info("count==================" + alloc);
            });
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }

        executor.shutdown();
    }
}

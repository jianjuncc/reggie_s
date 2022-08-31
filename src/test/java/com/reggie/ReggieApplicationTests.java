package com.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collections;
import java.util.Set;

@SpringBootTest
class ReggieApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("a", 45);
        System.out.println(redisTemplate.opsForValue().get("a"));
        redisTemplate.opsForSet().add("b", 77);
        System.out.println(redisTemplate.opsForSet().intersect(Collections.singleton("b")));


    }

    @Test
    void test() {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("001", "name", "李四");
        hashOperations.put("001", "age", "25");
    }

    @Test
    void list(){
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPush("info", "age");
        listOperations.leftPushAll("info","name","id");
        listOperations.range("info",0,-1).stream().forEach(System.out::println);
        System.out.println("--------------------------------------");
        for (Long i = Long.valueOf(0); i < listOperations.size("info"); i++) {
            Object info = listOperations.rightPop("info");
            System.out.println(info.toString());
        }
    }
    @Test
    public void Zest(){
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("info1", "e", 12.54);
        zSetOperations.add("info1", "g", 10.1);
        zSetOperations.add("info1", "f", 11.1);
        zSetOperations.range("info1",0,-1).forEach(System.out::println);
    }

    @Test
    void keys(){
        Set<String> keys = redisTemplate.keys("*");
        keys.forEach(System.out::println);

        Boolean user = redisTemplate.hasKey("a");
        System.out.println(user);

        Boolean user1 = redisTemplate.delete("a");
        System.out.println(user1);
    }
}

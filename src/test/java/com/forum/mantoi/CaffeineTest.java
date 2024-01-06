package com.forum.mantoi;

import com.github.benmanes.caffeine.cache.Cache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CaffeineTest {

    @Autowired
    Cache<String, Object> caffeineCache;

    @Test
    public void testCache() {
        checkCache(addCache());
    }

    public String addCache() {
        List<Integer> list = new ArrayList<>();
        list.add(12);
        list.add(24);
        list.add(36);
        String key = "list";
        caffeineCache.put(key, list);
        return key;
    }

    public void checkCache(String key) {
        List<Integer> list = (List<Integer>) caffeineCache.asMap().get(key);
        for (int x : list) {
            System.out.println(x);
        }
    }

}

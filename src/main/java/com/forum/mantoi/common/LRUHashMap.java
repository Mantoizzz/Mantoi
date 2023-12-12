package com.forum.mantoi.common;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 仅为学习用的自己实现的LRU缓存
 *
 * @param <K>
 * @param <V>
 */
public class LRUHashMap<K, V> {

    private final int cacheSize;

    private LinkedHashMap<K, V> cacheMap;

    public LRUHashMap(int cacheSize) {

        this.cacheSize = cacheSize;
        this.cacheMap = new LinkedHashMap<>(16, 0.75F, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return cacheSize + 1 == cacheMap.size();
            }
        };
    }

    public void set(K key, V value) {
        this.cacheMap.put(key, value);
    }

    public V get(K key) {
        return this.cacheMap.get(key);
    }

    public void delete(K key) {
        this.cacheMap.remove(key);
    }

}

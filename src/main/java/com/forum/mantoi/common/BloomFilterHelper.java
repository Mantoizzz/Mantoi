package com.forum.mantoi.common;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.function.Function;

/**
 * @author ChatGPT
 */
@AllArgsConstructor
public class BloomFilterHelper<T> {

    private BitSet bitSet;

    private int size;

    private Function<T, Integer>[] hashFunctions;

    public void add(T item) {
        for (Function<T, Integer> func : hashFunctions) {
            int index = func.apply(item) % size;
            bitSet.set(index, true);
        }
    }

    public boolean contains(T item) {
        for (Function<T, Integer> func : hashFunctions) {
            int index = func.apply(item) % size;
            if (!bitSet.get(index)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Function<String, Integer>[] arr = new Function[]{HashFunctions.hashFunction1(), HashFunctions.hashFunction2(), HashFunctions.hashFunction3()};
        BloomFilterHelper<String> bloomFilterHelper = new BloomFilterHelper<>(new BitSet(10000000), 10000000, arr);
        List<String> collectionA = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            int size = random.nextInt(1000);
            String str = RandomStringUtils.randomAlphabetic(size);
            collectionA.add(str);
            bloomFilterHelper.add(str);
        }
        int cnt = 0;
        for (int i = 0; i < 50000; i++) {
            int size = random.nextInt(1000);
            String str = RandomStringUtils.randomGraph(size);
            if (bloomFilterHelper.contains(str)) {
                cnt++;
            }
        }
        System.out.println(cnt);
    }
}
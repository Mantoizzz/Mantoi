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

}
package com.forum.mantoi.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

/**
 * @author DELL
 */
public class HashFunctions {

    private static final int M = 10000000;

    public static Function<String, Integer> hashFunction1() {
        return s -> {
            int hash = 0;
            for (int i = 0; i < s.length(); i++) {
                hash = (hash + s.charAt(i)) % M;
            }
            return hash;
        };
    }

    public static Function<String, Integer> hashFunction2() {
        return s -> s.hashCode() > 0 ? s.hashCode() : (-1) * s.hashCode();
    }

    public static Function<String, Integer> hashFunction3() {
        return s -> {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(s.getBytes());
                int result = 0;
                for (byte b : hash) {
                    result = (result * 31 + b) % 999;
                }
                return result > 0 ? result : (-1) * result;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return 0;
            }
        };
    }

}

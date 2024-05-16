package com.forum.mantoi.utils;


public final class NullValue {

    private static final NullValue INSTANCE = new NullValue();

    private NullValue() {

    }

    public static NullValue getInstance() {
        return INSTANCE;
    }
}

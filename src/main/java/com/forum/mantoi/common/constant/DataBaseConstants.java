package com.forum.mantoi.common.constant;

import lombok.Getter;

/**
 * @author DELL
 */
public class DataBaseConstants {

    public static class UserTable {

        public static final String COLUMN_USERNAME = "username";


        public static final String COLUMN_EMAIL = "email";
    }

    public static class PostTable {
        public static final String TABLE_NAME = "t_post";
    }

    public enum CommonColumnEnum {

        ID("id"),

        CREATE_TIME("create_time"),

        UPDATE_TIME("update_time");

        CommonColumnEnum(String name) {
        }

    }

    @Getter
    public enum SqlEnum {

        LIMIT_1("limit 1");

        private final String sql;

        SqlEnum(String sql) {
            this.sql = sql;
        }
    }

}

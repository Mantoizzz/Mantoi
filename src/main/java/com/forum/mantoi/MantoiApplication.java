package com.forum.mantoi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author DELL
 */
@SpringBootApplication
@MapperScan(value = "com.forum.mantoi.sys.dao.mapper")
public class MantoiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MantoiApplication.class, args);
    }


}

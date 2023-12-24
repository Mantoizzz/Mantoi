package com.forum.mantoi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringTests {
    @Test
    void test() {
        System.out.println("成功启动SpringBoot");
    }
}

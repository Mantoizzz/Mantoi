package com.forum.mantoi;

import com.forum.mantoi.sys.dao.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DaoTest {

    @Resource
    public UserMapper userMapper;

    @Test
    public void test() {

    }
}

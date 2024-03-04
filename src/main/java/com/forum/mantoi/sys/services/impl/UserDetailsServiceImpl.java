package com.forum.mantoi.sys.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forum.mantoi.common.constant.DataBaseConstants;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.dao.mapper.UserMapper;
import com.forum.mantoi.sys.exception.UserException;
import com.forum.mantoi.sys.model.JwtUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author DELL
 */
@AllArgsConstructor
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DataBaseConstants.UserTable.COLUMN_EMAIL, email);
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.nonNull(user)) {
            return new JwtUser(user);
        } else {
            throw new UserException(CommonResultStatus.FAIL, "User not found");
        }
    }
}

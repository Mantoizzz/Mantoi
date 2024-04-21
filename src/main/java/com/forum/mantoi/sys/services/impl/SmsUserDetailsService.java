package com.forum.mantoi.sys.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forum.mantoi.common.constant.DataBaseConstants;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.dao.mapper.UserMapper;
import com.forum.mantoi.sys.exception.UserException;
import com.forum.mantoi.sys.model.SysUser;
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
@Service("smsUserDetailsService")
@Slf4j
public class SmsUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DataBaseConstants.UserTable.COLUMN_PHONE, phone);
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.nonNull(user)) {
            return new SysUser(user);
        } else {
            throw new UserException(CommonResultStatus.RECORD_NOT_EXIST, "user not found");
        }
    }
}

package com.forum.mantoi.sys.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forum.mantoi.common.constant.DataBaseConstants;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.dao.mapper.UserMapper;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.services.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final UserMapper userMapper;

    @Override
    public List<?> search(String input, Class<?> clazz) {
        String name = clazz.getSimpleName();
        if ("User".equals(name)) {
            return searchUser(input);
        } else if ("Post".equals(name)) {
            return searchPost(input);
        } else {
            throw new BusinessException(CommonResultStatus.PARAM_ERROR, CommonResultStatus.PARAM_ERROR.getMsg());
        }
    }

    private List<User> searchUser(String input) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(DataBaseConstants.UserTable.COLUMN_USERNAME, input);
        return userMapper.selectList(queryWrapper);
    }

    //TODO
    private List<Post> searchPost(String input) {
        return null;
    }

}

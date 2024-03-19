package com.forum.mantoi.sys.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forum.mantoi.common.constant.DataBaseConstants;
import com.forum.mantoi.common.response.CommonResultStatus;
import com.forum.mantoi.sys.dao.entity.InvertIndex;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.PostContent;
import com.forum.mantoi.sys.dao.entity.User;
import com.forum.mantoi.sys.dao.mapper.PostMapper;
import com.forum.mantoi.sys.dao.mapper.UserMapper;
import com.forum.mantoi.sys.dao.repository.InvertIndexRepository;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.services.SearchService;
import com.forum.mantoi.utils.SearchTextUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final UserMapper userMapper;

    private final InvertIndexRepository repository;

    private final PostMapper postMapper;

    private final StringRedisTemplate redisTemplate;

    @Override
    public List<?> search(String input, Class<?> clazz) throws IOException {
        String name = clazz.getSimpleName();
        if ("User".equals(name)) {
            return searchUser(input);
        } else if ("Post".equals(name)) {
            return searchPost(input);
        } else {
            throw new BusinessException(CommonResultStatus.PARAM_ERROR, CommonResultStatus.PARAM_ERROR.getMsg());
        }
    }

    @Override
    public void saveDocument(Post post, PostContent content) throws IOException {
        Set<String> set = SearchTextUtils.tokenizationPost(post, content);
        for (String word : set) {
            if (SearchTextUtils.HIGH_FREQUENCY_WORDS.contains(word)) {
                redisTemplate.opsForValue().setBit(word, post.getId(), true);
            } else {
                InvertIndex index = repository.findInvertIndexByKeyword(word);
                long[] documents = index.getDocuments();
                long postId = post.getId();
                if (postId >= documents.length * 64L) {
                    int newLen = (int) (postId / 64L);
                    long[] newArr = new long[newLen];
                    System.arraycopy(documents, 0, newArr, 0, documents.length);
                    index.setDocuments(SearchTextUtils.process(postId, newArr));
                } else {
                    index.setDocuments(SearchTextUtils.process(postId, documents));
                }
                repository.insert(index);
            }
        }
    }

    private List<User> searchUser(String input) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(DataBaseConstants.UserTable.COLUMN_USERNAME, input);
        return userMapper.selectList(queryWrapper);
    }

    private List<Post> searchPost(String input) throws IOException {
        Set<String> set = SearchTextUtils.tokenizationInput(input);
        List<Post> result = new ArrayList<>();
        for (String str : set) {
            if (SearchTextUtils.HIGH_FREQUENCY_WORDS.contains(str)) {
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    Boolean bit = redisTemplate.opsForValue().getBit(str, i);
                    if (Boolean.TRUE.equals(bit)) {
                        result.add(postMapper.selectById(i));
                    }
                }
            } else {
                InvertIndex index = repository.findInvertIndexByKeyword(str);
                long[] documents = index.getDocuments();
                for (long l : documents) {
                    for (int j = 0; j < 64; j++) {
                        long bitValue = (l >>> j) & 1;
                        if (bitValue == 1) {
                            result.add(postMapper.selectById(bitValue));
                        }
                    }
                }
            }

        }
        return result;
    }

}

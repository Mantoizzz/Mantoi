package com.forum.mantoi.sys.services;

import com.forum.mantoi.sys.entity.User;
import org.springframework.stereotype.Service;

/**
 * 实体类接口
 * 针对用户、帖子、评论、跟帖
 *
 * @param <T>
 */
@Service
public interface PublishService<T> {

    T publish(User author, Object request);


    void delete(Long id);

    T modify(User author, T object, Object request);

}

package com.forum.mantoi.sys.services;

import com.forum.mantoi.sys.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface PublishService<T> {

    T publish(User author, Object request);

    void delete(Long id);

    T modify(User author, T object, Object request);

}

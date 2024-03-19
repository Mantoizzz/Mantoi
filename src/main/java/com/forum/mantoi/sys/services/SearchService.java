package com.forum.mantoi.sys.services;

import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.PostContent;

import java.io.IOException;
import java.util.List;

public interface SearchService {

    List<?> search(String input, Class<?> clazz) throws IOException;


    void saveDocument(Post post, PostContent content) throws IOException;

}

package com.forum.mantoi.sys.services;

import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.PostContent;
import com.forum.mantoi.sys.dao.entity.User;

import java.io.IOException;
import java.util.List;

/**
 * @author DELL
 */
public interface SearchService {

    List<User> searchUser(String input);

    List<Post> searchPost(String input) throws IOException;

    void saveDocument(Post post, PostContent content) throws IOException;

}

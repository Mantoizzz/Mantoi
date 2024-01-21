package com.forum.mantoi.sys.services;

import com.forum.mantoi.sys.entity.Post;
import com.forum.mantoi.sys.entity.Word;
import com.forum.mantoi.sys.repository.WordRepository;
import com.forum.mantoi.utils.SearchTextUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author DELL
 */
@Service
@AllArgsConstructor
public class MySearchService {

    private final WordRepository wordRepository;

    /**
     * 创建倒排索引
     *
     * @param post post
     * @throws IOException exception
     */
    public void makeIndex(Post post) throws IOException {
        List<String> list = SearchTextUtils.tokenizationPost(post);
        for (String str : list) {
            Optional<Word> word = wordRepository.findByContent(str);
            if (word.isPresent()) {
                word.get().getPosts().add(post);
                wordRepository.save(word.get());
                return;
            }
            Word word1 = new Word();
            word1.setPosts(new ArrayList<>());
            word1.setContent(str);
            wordRepository.save(word1);
        }
    }

    public List<Post> search(String request) throws IOException {
        List<String> strings = SearchTextUtils.tokenizationString(request);
        List<Post> searchResult = new ArrayList<>();
        for (String str : strings) {
            Optional<Word> wordOptional = wordRepository.findByContent(str);
            wordOptional.ifPresent(word -> searchResult.addAll(word.getPosts()));
        }
        return searchResult;
    }

}

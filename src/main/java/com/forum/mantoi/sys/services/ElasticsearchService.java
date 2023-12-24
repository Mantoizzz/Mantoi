package com.forum.mantoi.sys.services;

import com.forum.mantoi.sys.elasticsearch.EsPost;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ElasticsearchService {

    private final ElasticsearchOperations operations;

    public <T> void save(T obj) {
        operations.save(obj);
    }

    public <T> void delete(Class<T> clazz, long id) {
        String str = Long.toString(id);
        operations.delete(str, clazz);
    }

    public Page<EsPost> searchPost(String keyword, int current, int pageLimit) {
        Criteria criteria = new Criteria();
        String content = "content";
        String title = "title";
        criteria.and(content).and(title).fuzzy(keyword);
        HighlightField contentHigh = new HighlightField(content);
        HighlightField titleHigh = new HighlightField(title);
        Highlight highlight = new Highlight(List.of(contentHigh, titleHigh));
        Query query = new CriteriaQuery(criteria);
        query.addSort(Sort.by(Sort.Direction.DESC, "likes"));
        query.setHighlightQuery(new HighlightQuery(highlight, EsPost.class));
        SearchHits<EsPost> hits = operations.search(query, EsPost.class);
        Pageable pageable = PageRequest.of(current, pageLimit);
        return processSearchHits(hits, pageable);
    }

    private Page<EsPost> processSearchHits(SearchHits<EsPost> searchHits, Pageable pageable) {
        if (searchHits.getTotalHits() <= 0) {
            return Page.empty();
        }
        List<EsPost> list = new ArrayList<>();
        for (SearchHit<EsPost> hit : searchHits) {
            EsPost post = hit.getContent();
            List<String> contentList = hit.getHighlightFields().get("content");
            if (contentList != null && !contentList.isEmpty()) {
                String highlightContent = String.join(" ", contentList);
                post.setContent(highlightContent);
            }
            List<String> titleList = hit.getHighlightFields().get("title");
            if (titleList != null && !titleList.isEmpty()) {
                String highlightTitle = String.join(" ", titleList);
                post.setTitle(highlightTitle);
            }
            list.add(post);
        }
        return new PageImpl<>(list, pageable, searchHits.getTotalHits());
    }


}

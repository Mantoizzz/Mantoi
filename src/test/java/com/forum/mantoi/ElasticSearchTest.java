package com.forum.mantoi;

import com.forum.mantoi.sys.elasticsearch.EsPost;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.Date;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ElasticSearchTest {

    @Autowired
    private ElasticsearchOperations operations;


    private String save(EsPost post) {
        operations.save(post);
        return post.getTitle();
    }


    @Test
    public void test4Connect() {
        EsPost esPost = new EsPost();
        esPost.setId(1L);
        esPost.setTitle("love is justice?");
        esPost.setContent("never gonna give you up");
        esPost.setCreateTime(new Date());
        save(esPost);
    }

    @Test
    public void addData() {
        EsPost[] esPosts = new EsPost[100];
        long id = 2L;
        for (int i = 0; i < 100; i++) {
            EsPost esPost = new EsPost();
            esPost.setCreateTime(new Date());
            esPost.setId(id++);
            esPost.setTitle(RandomStringUtils.randomAlphabetic(10));
            esPost.setContent(RandomStringUtils.randomAlphabetic(20, 30));
            esPosts[i] = esPost;
        }
        operations.save(esPosts);
    }


    /**
     * 模糊查询
     */
    @Test
    public void test4Search() {
        String keyword = "love";
        String field = "title";
        Criteria criteria = new Criteria(field);
        criteria.fuzzy(keyword);
        Query query = new CriteriaQuery(criteria);
        SearchHits<EsPost> search = operations.search(query, EsPost.class);
        List<EsPost> list = search.stream().map(SearchHit::getContent).toList();
        for (EsPost esPost : list) {
            System.out.println(esPost.getTitle());
        }
    }

    @Test
    public void test4Spring() {
        System.out.println("123");
    }
}

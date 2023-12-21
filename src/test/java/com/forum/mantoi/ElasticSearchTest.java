package com.forum.mantoi;

import com.forum.mantoi.sys.elasticsearch.EsPost;
import com.forum.mantoi.sys.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ElasticSearchTest {

    @Autowired
    private ElasticsearchOperations operations;

    @Autowired
    private PostRepository postRepository;

    private String save(EsPost post) {
        operations.save(post);
        return post.getTitle();
    }


    @Test
    public void test4Connect() {
        EsPost esPost = new EsPost();
        esPost.setId(0L);
        esPost.setTitle("春招提前批");
        esPost.setContent("春招提前批，还有春招时间线，真假自测哦");
        esPost.setCreateTime(new Date());
        save(esPost);
    }

    @Test
    public void test4Spring() {
        System.out.println("123");
    }
}

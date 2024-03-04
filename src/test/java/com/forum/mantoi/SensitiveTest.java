package com.forum.mantoi;

import com.forum.mantoi.sys.handler.MyWordReplaceHandler;
import com.forum.mantoi.sys.services.SensitiveWordService;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Deprecated
public class SensitiveTest {

    @Autowired
    private SensitiveWordService service;

    @Test
    public void simpleTest() {
        final String text = "五星红旗迎风飘扬，毛主席的画像屹立在天安门前";
        List<String> all = SensitiveWordHelper.findAll(text);
        for (String s : all) {
            System.out.println(s);
        }
    }

    @Test
    public void customWords() {
        final String text = "国家主席习近平近日访问美国，讨论了中国失业的问题";
        System.out.println(service.replace(text));
    }
}

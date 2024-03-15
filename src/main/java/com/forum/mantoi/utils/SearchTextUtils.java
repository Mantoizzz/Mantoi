package com.forum.mantoi.utils;

import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.PostContent;
import lombok.experimental.UtilityClass;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * @author DELL
 */
@UtilityClass
public class SearchTextUtils {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList("和", "的", "与", "以及", "或者", "或", "还有", "虽然", "但是", "而且", "难道", "我", "你", "他"));

    public static String optimizeText(String text) {
        String lowerText = text.toLowerCase();
        String noPunctuationText = lowerText.replaceAll("\\p{Punct}", "");

        StringBuilder resultText = new StringBuilder();
        for (String word : noPunctuationText.split("\\s+")) {
            if (!STOP_WORDS.contains(word)) {
                resultText.append(word).append(" ");
            }
        }

        return resultText.toString().trim();
    }

    public static List<String> tokenizationString(String text) throws IOException {
        CharArraySet charArraySet = new CharArraySet(STOP_WORDS, true);
        Analyzer analyzer = new SmartChineseAnalyzer(charArraySet);
        StringReader stringReader = new StringReader(text);
        TokenStream tokenStream = analyzer.tokenStream("帖子分词", stringReader);
        CharTermAttribute attribute = tokenStream.addAttribute(CharTermAttribute.class);

        List<String> list = new ArrayList<>();
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            list.add(attribute.toString());
        }
        tokenStream.close();
        analyzer.close();
        return list;
    }

    public static List<String> tokenizationPost(Post post, PostContent content) throws IOException {
        return tokenizationString(post.getTitle() + content.getContent());
    }

}

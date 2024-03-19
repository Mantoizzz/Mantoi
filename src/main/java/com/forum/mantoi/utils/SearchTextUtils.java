package com.forum.mantoi.utils;

import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.PostContent;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author DELL
 */
@UtilityClass
@Slf4j
public class SearchTextUtils {

    private static final Set<String> STOP = new HashSet<>();

    public static final Set<String> HIGH_FREQUENCY_WORDS = Set.of("字节", "腾讯", "阿里巴巴", "小米", "华为", "京东", "美团",
            "快手", "小红书", "顺丰", "得物", "bilibili", "秋招", "春招", "蚂蚁", "塞力斯", "Momenta",
            "微软", "Nvidia");

    static {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/resources/cn_stopwords.txt"), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                STOP.add(line.trim());
            }
        } catch (Exception e) {
            log.error("Read STOP WORDS Error");
        }

    }

    private static String optimizeText(String text) {
        return text.toLowerCase().replaceAll("\\p{Punct}", "").trim();
    }

    private static Set<String> tokenizationString(String text) throws IOException {
        CharArraySet charArraySet = new CharArraySet(STOP, true);
        Analyzer analyzer = new SmartChineseAnalyzer(charArraySet);
        StringReader stringReader = new StringReader(optimizeText(text));
        TokenStream tokenStream = analyzer.tokenStream("分词", stringReader);
        CharTermAttribute attribute = tokenStream.addAttribute(CharTermAttribute.class);

        Set<String> set = new HashSet<>();
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            set.add(attribute.toString());
        }
        tokenStream.close();
        analyzer.close();
        return set;
    }

    public static Set<String> tokenizationPost(Post post, PostContent content) throws IOException {
        return tokenizationString(post.getTitle() + content.getContent());
    }

    public static Set<String> tokenizationInput(String input) throws IOException {
        return tokenizationString(input);
    }

    public static long[] process(long id, long[] document) {
        int count = (int) (id / 64);
        int rest = count & 63;
        document[count] = document[count] | (1L << rest);
        return document;
    }

}

package com.forum.mantoi.sys.handler;

import com.github.houbb.sensitive.word.api.IWordContext;
import com.github.houbb.sensitive.word.api.IWordReplace;
import com.github.houbb.sensitive.word.api.IWordResult;
import com.github.houbb.sensitive.word.utils.InnerWordCharUtils;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DELL
 */
@Deprecated
public class MyWordReplaceHandler implements IWordReplace {

    private Map<String, String> replaceMap;

    @Getter
    public static MyWordReplaceHandler instance = new MyWordReplaceHandler();

    public MyWordReplaceHandler init() {
        this.replaceMap = new HashMap<>();
        replaceMap.put("失业", "灵活就业");
        return this;
    }

    private MyWordReplaceHandler() {

    }

    @Override
    public void replace(StringBuilder stringBuilder, char[] chars, IWordResult iWordResult, IWordContext iWordContext) {
        String word = InnerWordCharUtils.getString(chars, iWordResult);
        if (replaceMap.containsKey(word)) {
            stringBuilder.append(replaceMap.get(word));
        } else {
            int len = iWordResult.endIndex() - iWordResult.startIndex();
            stringBuilder.append("*".repeat(Math.max(0, len)));
        }
    }
}

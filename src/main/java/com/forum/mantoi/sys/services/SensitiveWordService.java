package com.forum.mantoi.sys.services;

import com.forum.mantoi.sys.handler.MyWordReplaceHandler;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SensitiveWordService {

    private final MyWordReplaceHandler wordReplace;

    /**
     * 使用自定义Handler来进行替换
     *
     * @param text text
     * @return result
     */
    public String replace(String text) {
        return SensitiveWordHelper.replace(text, wordReplace);
    }
}

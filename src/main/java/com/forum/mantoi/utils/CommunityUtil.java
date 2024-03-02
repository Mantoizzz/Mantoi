package com.forum.mantoi.utils;

import com.alibaba.fastjson.JSONObject;
import com.forum.mantoi.common.response.CommonResultStatus;

import java.util.Map;

/**
 * @author DELL
 */
public class CommunityUtil {

    public static String getJsonString(int code, String msg, Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                jsonObject.put(key, map.get(key));
            }
        }
        return jsonObject.toJSONString();
    }

    public static String getJsonString(CommonResultStatus commonResultStatus) {
        return getJsonString(commonResultStatus.getCode(), commonResultStatus.getMsg(), null);
    }


}

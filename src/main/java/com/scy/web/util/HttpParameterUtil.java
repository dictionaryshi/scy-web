package com.scy.web.util;

import com.scy.core.ArrayUtil;
import com.scy.core.CollectionUtil;
import com.scy.core.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * HttpParameterUtil
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/18.
 */
public class HttpParameterUtil {

    private HttpParameterUtil() {
    }

    public static Map<String, Object> getParameterMap(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (CollectionUtil.isEmpty(parameterMap)) {
            return CollectionUtil.emptyMap();
        }

        Map<String, Object> result = CollectionUtil.newHashMap();
        parameterMap.forEach((key, valueArray) -> {
            String value;
            if (ArrayUtil.isEmpty(valueArray)) {
                value = StringUtil.EMPTY;
            } else {
                value = valueArray[0];
            }
            result.put(key, value);
        });

        return result;
    }
}

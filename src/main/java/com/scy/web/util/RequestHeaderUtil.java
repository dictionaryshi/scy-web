package com.scy.web.util;

import com.scy.core.CollectionUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * RequestHeaderUtil
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/18.
 */
public class RequestHeaderUtil {

    private RequestHeaderUtil() {
    }

    public static Map<String, String> getRequestHeaderMap(HttpServletRequest request) {
        Map<String, String> headerMap = CollectionUtil.newHashMap();
        List<String> headerNames = CollectionUtil.enumeration2List(request.getHeaderNames());
        headerNames.forEach(headerName -> headerMap.put(headerName, request.getHeader(headerName)));
        return headerMap;
    }
}

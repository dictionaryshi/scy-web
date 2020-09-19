package com.scy.web.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.net.HttpHeaders;
import com.scy.core.ArrayUtil;
import com.scy.core.CollectionUtil;
import com.scy.core.ObjectUtil;
import com.scy.core.StringUtil;
import com.scy.core.json.JsonUtil;
import com.scy.core.trace.TraceUtil;
import com.scy.web.util.HttpParameterUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * WebFilter
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/19.
 */
public class WebFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            // 需在此初始化
            servletRequest.getParameterMap();

            FilterHttpServletRequest filterHttpServletRequest = new FilterHttpServletRequest((HttpServletRequest) servletRequest);

            init(filterHttpServletRequest);

            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setHeader(HttpHeaders.X_FRAME_OPTIONS, "DENY");

            filterChain.doFilter(filterHttpServletRequest, servletResponse);
        } finally {
            TraceUtil.clearTrace();
        }
    }

    private void init(FilterHttpServletRequest filterHttpServletRequest) {
        TraceUtil.setTraceId(filterHttpServletRequest.getHeader(TraceUtil.TRACE_ID));

        saveParams(filterHttpServletRequest);
    }

    private void saveParams(FilterHttpServletRequest filterHttpServletRequest) {
        Map<String, Object> paramMap = CollectionUtil.newHashMap();

        Map<String, Object> parameterMap = HttpParameterUtil.getParameterMap(filterHttpServletRequest);
        paramMap.putAll(parameterMap);

        Map<String, Object> bodyMap = getBodyMap(filterHttpServletRequest);
        paramMap.putAll(bodyMap);

        HttpParameterUtil.setParamMap(paramMap);
    }

    private Map<String, Object> getBodyMap(FilterHttpServletRequest filterHttpServletRequest) {
        String contentType = filterHttpServletRequest.getContentType();
        if (StringUtil.isEmpty(contentType)) {
            return CollectionUtil.emptyMap();
        }
        if (!contentType.toLowerCase().contains(JsonUtil.JSON)) {
            return CollectionUtil.emptyMap();
        }

        byte[] bodyBytes = filterHttpServletRequest.getBodyBytes();
        if (ArrayUtil.isEmpty(bodyBytes)) {
            return CollectionUtil.emptyMap();
        }

        String paramJson = new String(bodyBytes);
        Map<String, Object> bodyMap = JsonUtil.json2Object(paramJson, new TypeReference<Map<String, Object>>() {
        });
        if (ObjectUtil.isNull(bodyMap)) {
            bodyMap = CollectionUtil.newHashMap();
        }
        return bodyMap;
    }

    @Override
    public void destroy() {
    }
}

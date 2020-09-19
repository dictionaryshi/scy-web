package com.scy.web.util;

import com.scy.core.SystemUtil;
import com.scy.core.json.JsonUtil;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * HttpMessageConverterUtil
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/19.
 */
public class HttpMessageConverterUtil {

    private HttpMessageConverterUtil() {
    }

    public static void initHttpMessageConverter(HttpMessageConverter<?> httpMessageConverter) {
        if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) httpMessageConverter;
            mappingJackson2HttpMessageConverter.setObjectMapper(JsonUtil.getBaseObjectMapper());
        } else if (httpMessageConverter instanceof StringHttpMessageConverter) {
            StringHttpMessageConverter stringHttpMessageConverter = (StringHttpMessageConverter) httpMessageConverter;
            // 去除响应中服务器可接收编码
            stringHttpMessageConverter.setWriteAcceptCharset(Boolean.FALSE);
            stringHttpMessageConverter.setDefaultCharset(SystemUtil.CHARSET_UTF_8);
        }
    }
}

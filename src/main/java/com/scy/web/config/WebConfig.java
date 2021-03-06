package com.scy.web.config;

import com.scy.core.enums.OrderEnum;
import com.scy.core.thread.ThreadPoolUtil;
import com.scy.web.aspect.ControllerAspect;
import com.scy.web.aspect.ResubmitCheckAspect;
import com.scy.web.filter.WebFilter;
import com.scy.web.util.HttpMessageConverterUtil;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

import javax.annotation.PostConstruct;
import javax.servlet.MultipartConfigElement;
import java.util.Collections;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * WebConfig
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/18.
 */
public class WebConfig {

    @Autowired
    private ObjectFactory<HttpMessageConverters> httpMessageConvertersObjectFactory;

    @PostConstruct
    public void afterHttpMessageConvertersObjectFactoryAutowired() {
        httpMessageConvertersObjectFactory.getObject().getConverters().forEach(HttpMessageConverterUtil::initHttpMessageConverter);
    }

    @Bean
    public CloseApplicationListener closeApplicationListener() {
        return new CloseApplicationListener();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 单个文件最大
        factory.setMaxFileSize(DataSize.parse("5MB"));
        /// 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.parse("20MB"));
        return factory.createMultipartConfig();
    }

    @Bean
    public FilterRegistrationBean<WebFilter> webFilter() {
        FilterRegistrationBean<WebFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new WebFilter());
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/*"));
        filterRegistrationBean.setOrder(OrderEnum.SYSTEM_START.getOrder());
        return filterRegistrationBean;
    }

    @Bean
    public ControllerAspect controllerAspect() {
        return new ControllerAspect();
    }

    @Bean
    public WebMvcConfig webMvcConfig() {
        return new WebMvcConfig();
    }

    @Bean
    public ThreadPoolExecutor zkLockThreadPoolExecutor() {
        return ThreadPoolUtil.getThreadPool("zk-lock", 1, 1, 10);
    }

    @Bean
    public ResubmitCheckAspect resubmitCheckAspect() {
        return new ResubmitCheckAspect();
    }
}

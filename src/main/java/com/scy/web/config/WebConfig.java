package com.scy.web.config;

import org.springframework.context.annotation.Bean;

/**
 * WebConfig
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/18.
 */
public class WebConfig {

    @Bean
    public CloseApplicationListener closeApplicationListener() {
        return new CloseApplicationListener();
    }
}

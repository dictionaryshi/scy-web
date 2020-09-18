package com.scy.web.config;

import com.scy.core.spring.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * SpringBootRunListener
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/18.
 */
@Slf4j
public class SpringBootRunListener implements SpringApplicationRunListener {

    public SpringBootRunListener(SpringApplication springApplication, String[] args) {
    }

    @Override
    public void starting() {
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        ApplicationContextUtil.setApplicationContext(context);
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
    }
}

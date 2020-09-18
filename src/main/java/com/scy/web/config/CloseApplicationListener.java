package com.scy.web.config;

import com.scy.core.format.MessageUtil;
import com.scy.core.thread.ThreadPoolUtil;
import com.scy.web.util.TomcatThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * CloseApplicationListener
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/18.
 */
@Slf4j
public class CloseApplicationListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        closeTomcatPool();
    }

    private void closeTomcatPool() {
        ThreadPoolExecutor tomcatThreadPool = TomcatThreadPoolUtil.getTomcatThreadPool();
        String poolName = "tomcat-pool";
        try {
            ThreadPoolUtil.shutdown(tomcatThreadPool, poolName);
        } catch (Throwable e) {
            log.error(MessageUtil.format("thread pool shutdown error", e, "poolName", poolName, "thread", Thread.currentThread().getName()));
        }
    }
}

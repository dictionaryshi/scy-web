package com.scy.web.util;

import com.scy.core.spring.ApplicationContextUtil;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * TomcatThreadPoolUtil
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/18.
 */
public class TomcatThreadPoolUtil {

    private TomcatThreadPoolUtil() {
    }

    @Nullable
    public static ThreadPoolExecutor getTomcatThreadPool() {
        ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
        if (!(applicationContext instanceof AnnotationConfigServletWebServerApplicationContext)) {
            return null;
        }
        AnnotationConfigServletWebServerApplicationContext annotationConfigServletWebServerApplicationContext = (AnnotationConfigServletWebServerApplicationContext) applicationContext;
        TomcatWebServer tomcatWebServer = (TomcatWebServer) annotationConfigServletWebServerApplicationContext.getWebServer();
        return (ThreadPoolExecutor) tomcatWebServer.getTomcat().getConnector().getProtocolHandler().getExecutor();
    }
}

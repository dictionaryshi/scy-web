package com.scy.web.util;

import com.scy.core.spring.ApplicationContextUtil;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

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

    public static ThreadPoolExecutor getTomcatThreadPool() {
        AnnotationConfigServletWebServerApplicationContext annotationConfigServletWebServerApplicationContext = (AnnotationConfigServletWebServerApplicationContext) ApplicationContextUtil.getApplicationContext();
        TomcatWebServer tomcatWebServer = (TomcatWebServer) annotationConfigServletWebServerApplicationContext.getWebServer();
        return (ThreadPoolExecutor) tomcatWebServer.getTomcat().getConnector().getProtocolHandler().getExecutor();
    }
}

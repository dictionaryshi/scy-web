package com.scy.web.util;

import com.google.common.net.HttpHeaders;
import com.scy.core.CollectionUtil;
import com.scy.core.StringUtil;
import com.scy.core.net.NetworkInterfaceUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * IpUtil
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/18.
 */
public class IpUtil {

    public static final List<String> LOCAL_HOSTS = CollectionUtil.unmodifiableList(CollectionUtil.newArrayList(
            "0:0:0:0:0:0:0:1", "127.0.0.1"
    ));

    public static final String X_FORWARDED_FOR = HttpHeaders.X_FORWARDED_FOR;

    private IpUtil() {
    }

    public static String getIp(HttpServletRequest request) {
        String xforwardedForHeader = request.getHeader(X_FORWARDED_FOR);
        if (!StringUtil.isEmpty(xforwardedForHeader)) {
            return xforwardedForHeader;
        }
        String remoteAddr = request.getRemoteAddr();
        if (!LOCAL_HOSTS.contains(remoteAddr)) {
            return remoteAddr;
        }
        return NetworkInterfaceUtil.getIp();
    }
}

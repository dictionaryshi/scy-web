package com.scy.web.util;

import com.scy.core.StringUtil;
import com.scy.core.thread.ThreadLocalUtil;
import com.scy.web.model.UserBO;

import javax.servlet.http.HttpServletRequest;

/**
 * LoginUtil
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/20.
 */
public class LoginUtil {

    private LoginUtil() {
    }

    public static final String TOKEN = "token";

    public static final String LOGIN_USER = "login_user";

    public static final String COOKIE_SSO = "SCY_SSO";

    public static String getToken(HttpServletRequest request) {
        // 从请求参数中获取
        String loginToken = request.getParameter(TOKEN);
        if (!StringUtil.isEmpty(loginToken)) {
            return loginToken;
        }

        // 从header中获取
        loginToken = request.getHeader(TOKEN);
        if (!StringUtil.isEmpty(loginToken)) {
            return loginToken;
        }

        // 从cookie中获取
        loginToken = CookieUtil.getCookieValue(request, COOKIE_SSO);

        return loginToken;
    }

    public static UserBO getLoginUser() {
        return (UserBO) ThreadLocalUtil.get(LOGIN_USER);
    }

    public static void setLoginUser(UserBO userBO) {
        ThreadLocalUtil.put(LOGIN_USER, userBO);
    }
}

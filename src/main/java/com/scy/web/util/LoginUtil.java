package com.scy.web.util;

import com.scy.core.ObjectUtil;
import com.scy.core.StringUtil;
import com.scy.core.enums.ResponseCodeEnum;
import com.scy.core.exception.BusinessException;
import com.scy.core.reflect.AnnotationUtil;
import com.scy.core.thread.ThreadLocalUtil;
import com.scy.web.annotation.LoginCheck;
import com.scy.web.model.UserBO;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

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

    public static void loginCheck(HttpServletRequest request, Method method) {
        LoginCheck loginCheck = AnnotationUtil.findAnnotation(method, LoginCheck.class);
        if (ObjectUtil.isNull(loginCheck)) {
            return;
        }

        String errorMessage = "请检查登陆账号是否正常, 并尝试重新登陆";

        String token = getToken(request);
        if (StringUtil.isEmpty(token)) {
            throw new BusinessException(ResponseCodeEnum.SYSTEM_EXCEPTION.getCode(), "getToken error");
        }

        UserBO userBO = new UserBO();

        setLoginUser(userBO);
    }
}

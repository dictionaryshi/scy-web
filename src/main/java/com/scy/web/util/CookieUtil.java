package com.scy.web.util;

import com.scy.core.ArrayUtil;
import com.scy.core.StringUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Stream;

/**
 * CookieUtil
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/18.
 */
public class CookieUtil {

    private CookieUtil() {
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (ArrayUtil.isEmpty(cookies)) {
            return StringUtil.EMPTY;
        }

        Cookie findCookie = Stream.of(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        return findCookie == null ? StringUtil.EMPTY : findCookie.getValue();
    }

    public static void sendCookie(
            HttpServletResponse response,
            String name,
            String value,
            String domain
    ) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setMaxAge(-1);
        cookie.setPath("/");
        cookie.setHttpOnly(Boolean.TRUE);
        response.addCookie(cookie);
    }
}

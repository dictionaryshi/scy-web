package com.scy.web.filter;

import com.scy.core.IOUtil;
import com.scy.core.SystemUtil;
import lombok.Getter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * FilterHttpServletRequest
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/19.
 */
@Getter
public class FilterHttpServletRequest extends HttpServletRequestWrapper {

    private final byte[] bodyBytes;

    public FilterHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        this.bodyBytes = IOUtil.toByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ByteArrayServletInputStream(bodyBytes);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), SystemUtil.CHARSET_UTF_8));
    }
}

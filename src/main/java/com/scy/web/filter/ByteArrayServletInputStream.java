package com.scy.web.filter;

import com.scy.core.format.NumberUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * ByteArrayServletInputStream
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/19.
 */
public class ByteArrayServletInputStream extends ServletInputStream {

    private final ByteArrayInputStream byteArrayInputStream;

    public ByteArrayServletInputStream(byte[] bytes) {
        this.byteArrayInputStream = new ByteArrayInputStream(bytes);
    }

    @Override
    public boolean isFinished() {
        return byteArrayInputStream.available() == NumberUtil.ZERO.intValue();
    }

    @Override
    public boolean isReady() {
        return Boolean.TRUE;
    }

    @Override
    public void setReadListener(ReadListener listener) {
    }

    @Override
    public int read() throws IOException {
        return byteArrayInputStream.read();
    }
}

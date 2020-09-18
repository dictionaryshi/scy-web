package com.scy.web.util;

import com.google.common.net.HttpHeaders;
import com.scy.core.encode.UrlEncodeUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DownloadUtil
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/18.
 */
public class DownloadUtil {

    private DownloadUtil() {
    }

    public static final String CONTENT_DISPOSITION = HttpHeaders.CONTENT_DISPOSITION;

    public static void download(HttpServletRequest request, HttpServletResponse response, String fileName, byte[] fileBytes) throws Throwable {
        String mimeType = request.getServletContext().getMimeType(fileName);

        response.setHeader(CONTENT_DISPOSITION, "attachment; filename=" + UrlEncodeUtil.urlEncode(fileName));

        // 设置mime类型
        response.setContentType(mimeType);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(fileBytes);
        outputStream.flush();
    }
}

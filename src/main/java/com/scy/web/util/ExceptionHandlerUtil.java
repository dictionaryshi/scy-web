package com.scy.web.util;

import com.scy.core.StringUtil;
import com.scy.core.enums.ResponseCodeEnum;
import com.scy.core.exception.BusinessException;
import com.scy.core.format.MessageUtil;
import com.scy.core.rest.ResponseResult;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * ExceptionHandlerUtil
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/20.
 */
@Slf4j
public class ExceptionHandlerUtil {

    private ExceptionHandlerUtil() {
    }

    public static final String BROKEN_PIPE = "broken pipe";

    public static ResponseResult<?> exception2ResponseResult(
            HttpServletRequest request,
            Throwable throwable
    ) {
        if (throwable instanceof BusinessException) {
            return handleBusinessException((BusinessException) throwable);
        }

        ResponseResult<?> result = ResponseResult.error(ResponseCodeEnum.SYSTEM_EXCEPTION.getCode(), throwable.getMessage(), null);
        if (!StringUtil.isEmpty(throwable.getMessage()) && throwable.getMessage().toLowerCase().contains(BROKEN_PIPE)) {
            log.warn(MessageUtil.format(BROKEN_PIPE, throwable, "url", request.getRequestURL().toString()));
        } else {
            log.error(MessageUtil.format("http error", throwable, "url", request.getRequestURL().toString()));
        }
        return result;
    }

    private static ResponseResult<?> handleBusinessException(BusinessException businessException) {
        ResponseResult<?> responseResult = ResponseResult.error(businessException.getCode(), businessException.getMessage(), null);
        log.info(MessageUtil.format("businessException", businessException.getThrowable(), "result", responseResult.toString()));
        return responseResult;
    }
}

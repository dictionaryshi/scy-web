package com.scy.web.util;

import com.scy.core.ObjectUtil;
import com.scy.core.StringUtil;
import com.scy.core.enums.ResponseCodeEnum;
import com.scy.core.exception.BusinessException;
import com.scy.core.format.MessageUtil;
import com.scy.core.rest.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

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

        if (throwable instanceof MissingServletRequestParameterException) {
            return handleMissingServletRequestParameterException((MissingServletRequestParameterException) throwable);
        }

        if (throwable instanceof HttpRequestMethodNotSupportedException) {
            return handleHttpRequestMethodNotSupportedException((HttpRequestMethodNotSupportedException) throwable);
        }

        if (throwable instanceof BindException) {
            return handleBindException((BindException) throwable);
        }

        if (throwable instanceof HttpMessageNotReadableException) {
            return handleHttpMessageNotReadableException((HttpMessageNotReadableException) throwable);
        }

        if (throwable instanceof MethodArgumentNotValidException) {
            return handleMethodArgumentNotValidException((MethodArgumentNotValidException) throwable);
        }

        if (throwable instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException) throwable);
        }

        ResponseResult<?> result = ResponseResult.error(ResponseCodeEnum.SYSTEM_EXCEPTION.getCode(), throwable.getMessage(), null);
        if (!StringUtil.isEmpty(throwable.getMessage()) && throwable.getMessage().toLowerCase().contains(BROKEN_PIPE)) {
            log.warn(MessageUtil.format(BROKEN_PIPE, throwable, "url", request.getRequestURL().toString()));
        } else {
            log.error(MessageUtil.format("http error", throwable, "url", request.getRequestURL().toString()));
        }
        return result;
    }

    private static ResponseResult<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
        ResponseResult<?> responseResult = ResponseResult.error(ResponseCodeEnum.PARAMS_EXCEPTION.getCode(), MessageUtil.format("methodArgumentTypeMismatchException",
                "field", methodArgumentTypeMismatchException.getName(), "value", ObjectUtil.obj2Str(methodArgumentTypeMismatchException.getValue())), null);
        log.info(MessageUtil.format("methodArgumentTypeMismatchException", "result", responseResult.toString()));
        return responseResult;
    }

    private static ResponseResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        FieldError fieldError = fieldErrors.get(0);
        ResponseResult<?> responseResult = ResponseResult.error(ResponseCodeEnum.PARAMS_EXCEPTION.getCode(), MessageUtil.format("methodArgumentNotValidException",
                "field", fieldError.getField(), "value", fieldError.getRejectedValue() == null ? null : fieldError.getRejectedValue().toString(),
                "message", fieldError.getDefaultMessage()), null);
        log.info(MessageUtil.format("methodArgumentNotValidException", "result", responseResult.toString()));
        return responseResult;
    }

    private static ResponseResult<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException) {
        ResponseResult<?> responseResult = ResponseResult.error(ResponseCodeEnum.PARAMS_EXCEPTION.getCode(), MessageUtil.format("httpMessageNotReadableException",
                "message", httpMessageNotReadableException.getMessage()), null);
        log.info(MessageUtil.format("httpMessageNotReadableException", "result", responseResult.toString()));
        return responseResult;
    }

    private static ResponseResult<?> handleBindException(BindException bindException) {
        List<FieldError> fieldErrors = bindException.getFieldErrors();
        FieldError fieldError = fieldErrors.get(0);
        ResponseResult<?> responseResult = ResponseResult.error(ResponseCodeEnum.PARAMS_EXCEPTION.getCode(), MessageUtil.format("bindException",
                "field", fieldError.getField(), "value", fieldError.getRejectedValue() == null ? null : fieldError.getRejectedValue().toString(),
                "message", fieldError.getDefaultMessage()), null);
        log.info(MessageUtil.format("bindException", "result", responseResult.toString()));
        return responseResult;
    }

    private static ResponseResult<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
        ResponseResult<?> responseResult = ResponseResult.error(ResponseCodeEnum.SYSTEM_EXCEPTION.getCode(), MessageUtil.format(httpRequestMethodNotSupportedException.getMessage(),
                "supportedMethods", Arrays.toString(httpRequestMethodNotSupportedException.getSupportedMethods())), null);
        log.info(MessageUtil.format("httpRequestMethodNotSupportedException", "result", responseResult.toString()));
        return responseResult;
    }

    private static ResponseResult<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException missingServletRequestParameterException) {
        ResponseResult<?> responseResult = ResponseResult.error(ResponseCodeEnum.PARAMS_EXCEPTION.getCode(), MessageUtil.format("缺少参数",
                "parameterName", missingServletRequestParameterException.getParameterName(), "parameterType", missingServletRequestParameterException.getParameterType()), null);
        log.info(MessageUtil.format("missingServletRequestParameterException", "result", responseResult.toString()));
        return responseResult;
    }

    private static ResponseResult<?> handleBusinessException(BusinessException businessException) {
        ResponseResult<?> responseResult = ResponseResult.error(businessException.getCode(), businessException.getMessage(), null);
        log.info(MessageUtil.format("businessException", businessException.getThrowable(), "result", responseResult.toString()));
        return responseResult;
    }
}

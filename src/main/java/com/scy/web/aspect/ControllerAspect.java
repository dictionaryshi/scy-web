package com.scy.web.aspect;

import com.scy.core.ObjectUtil;
import com.scy.core.StringUtil;
import com.scy.core.enums.ResponseCodeEnum;
import com.scy.core.exception.BusinessException;
import com.scy.core.format.MessageUtil;
import com.scy.core.model.JoinPointBO;
import com.scy.core.spring.JoinPointUtil;
import com.scy.web.model.RequestLogAO;
import com.scy.web.util.IpUtil;
import com.scy.web.util.LoginUtil;
import com.scy.web.util.RsaCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * ControllerAspect
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/20.
 */
@Slf4j
@Order(ControllerAspect.SYSTEM_START)
@Aspect
public class ControllerAspect {

    /**
     * 系统入口切面
     */
    public static final int SYSTEM_START = 0;

    @Pointcut("(" +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping) "
            + "|| @annotation(org.springframework.web.bind.annotation.GetMapping) "
            + "|| @annotation(org.springframework.web.bind.annotation.PostMapping) "
            + "|| @annotation(org.springframework.web.bind.annotation.PutMapping) "
            + "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping) "
            + ") && !execution(* com.scy..feign.*.*(..)) "
    )
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        HttpServletRequest request = getRequest();

        RequestLogAO requestLogAO = new RequestLogAO();
        requestLogAO.setRequest(request);

        JoinPointBO joinPointBO = JoinPointUtil.getJoinPointBO(proceedingJoinPoint);
        requestLogAO.setJoinPointBO(joinPointBO);

        long startTime = System.currentTimeMillis();
        requestLogAO.setStartTime(startTime);

        String ip = IpUtil.getIp(request);
        requestLogAO.setIp(ip);

        log.info(MessageUtil.format("http request",
                "ip", requestLogAO.getIp(), "url", requestLogAO.getRequest().getRequestURL().toString(), "method", requestLogAO.getJoinPointBO().getMethodName(),
                "params", requestLogAO.getJoinPointBO().getParams()));

        try {
            LoginUtil.loginCheck(requestLogAO.getRequest(), requestLogAO.getJoinPointBO().getMethod());

            RsaCheckUtil.signCheck(requestLogAO.getRequest(), requestLogAO.getJoinPointBO().getMethod());

            Object result = proceedingJoinPoint.proceed();
            log.info(MessageUtil.format("http response",
                    "ip", requestLogAO.getIp(), "url", requestLogAO.getRequest().getRequestURL().toString(), "method", requestLogAO.getJoinPointBO().getMethodName(),
                    "params", requestLogAO.getJoinPointBO().getParams(), StringUtil.COST, System.currentTimeMillis() - requestLogAO.getStartTime(), "result", result));
            return result;
        } catch (Throwable throwable) {
            return null;
        }
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNull(servletRequestAttributes)) {
            throw new BusinessException(ResponseCodeEnum.SYSTEM_EXCEPTION.getCode(), "getRequest error");
        }
        return servletRequestAttributes.getRequest();
    }
}

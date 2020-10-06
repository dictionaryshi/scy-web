package com.scy.web.aspect;

import com.scy.core.ObjectUtil;
import com.scy.core.StringUtil;
import com.scy.core.enums.ResponseCodeEnum;
import com.scy.core.exception.BusinessException;
import com.scy.web.util.HttpParameterUtil;
import com.scy.zookeeper.ZkClient;
import com.scy.zookeeper.ZkLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ResubmitCheckAspect
 *
 * @author shichunyang
 * Created by shichunyang on 2020/10/6.
 */
@Slf4j
@Order(ResubmitCheckAspect.REQUEST_RESUBMIT)
@Aspect
public class ResubmitCheckAspect {

    public static final int REQUEST_RESUBMIT = 12000;

    @Autowired
    private ZkClient zkClient;

    @Autowired
    private ThreadPoolExecutor zkLockThreadPoolExecutor;

    @Pointcut("@annotation(com.scy.web.annotation.ResubmitCheck)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Map<String, Object> paramMap = HttpParameterUtil.getParamMap();
        String resubmitToken = ObjectUtil.obj2Str(paramMap.get("resubmitToken"));
        if (StringUtil.isEmpty(resubmitToken)) {
            throw new BusinessException(ResponseCodeEnum.SYSTEM_EXCEPTION.getCode(), "缺少重复提交验证参数");
        }

        ZkLock zkLock = new ZkLock(zkClient, zkLockThreadPoolExecutor);
        try {
            zkLock.lock(resubmitToken);
            return joinPoint.proceed();
        } finally {
            zkLock.unlock();
        }
    }
}

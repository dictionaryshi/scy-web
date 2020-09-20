package com.scy.web.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

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
}

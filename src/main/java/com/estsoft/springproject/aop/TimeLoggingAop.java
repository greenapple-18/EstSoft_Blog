package com.estsoft.springproject.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class TimeLoggingAop {
    @Around("execution(* com.estsoft.springproject.bookproject..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTimeMs = System.currentTimeMillis();
        log.info("START: {}" ,joinPoint);
        try {
            return joinPoint.proceed();
        } finally {
            long finishTimeMs = System.currentTimeMillis();
            long timeMs = finishTimeMs - startTimeMs;
            log.info("END: {} {}ms", joinPoint, timeMs);
        }
    }
}

package com.volosinzena.barolab.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ScheduledJobLoggingAspect {

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object logScheduledJob(ProceedingJoinPoint joinPoint) throws Throwable {
        String target = joinPoint.getSignature().toShortString();
        long startNs = System.nanoTime();
        log.info("Scheduled job started target={}", target);
        try {
            Object result = joinPoint.proceed();
            long durationMs = (System.nanoTime() - startNs) / 1_000_000;
            log.info("Scheduled job finished target={} durationMs={}", target, durationMs);
            return result;
        } catch (Throwable ex) {
            long durationMs = (System.nanoTime() - startNs) / 1_000_000;
            log.error("Scheduled job failed target={} durationMs={} error={}", target, durationMs, ex.toString());
            throw ex;
        }
    }
}

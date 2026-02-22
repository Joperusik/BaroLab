package com.volosinzena.barolab.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class SlowOperationLoggingAspect {

    @Value("${app.logging.slow-operation-ms:500}")
    private long slowOperationMs;

    @Around("execution(* com.volosinzena.barolab.service..*(..))")
    public Object logSlowOperations(ProceedingJoinPoint joinPoint) throws Throwable {
        long startNs = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long durationMs = (System.nanoTime() - startNs) / 1_000_000;
            if (durationMs >= slowOperationMs) {
                String target = joinPoint.getSignature().toShortString();
                log.warn("Slow operation target={} durationMs={}", target, durationMs);
            }
        }
    }
}

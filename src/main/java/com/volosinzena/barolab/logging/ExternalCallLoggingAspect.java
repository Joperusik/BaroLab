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
public class ExternalCallLoggingAspect {

    @Value("${app.logging.slow-external-ms:200}")
    private long slowExternalMs;

    @Around("execution(* com.volosinzena.barolab.repository..*(..))")
    public Object logRepositoryCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        long startNs = System.nanoTime();
        String target = joinPoint.getSignature().toShortString();

        try {
            Object result = joinPoint.proceed();
            long durationMs = (System.nanoTime() - startNs) / 1_000_000;
            if (durationMs >= slowExternalMs) {
                log.warn("External call slow target={} durationMs={}", target, durationMs);
            } else {
                log.info("External call ok target={} durationMs={}", target, durationMs);
            }
            return result;
        } catch (Throwable ex) {
            long durationMs = (System.nanoTime() - startNs) / 1_000_000;
            log.warn("External call failed target={} durationMs={} error={}", target, durationMs, ex.toString());
            throw ex;
        }
    }
}

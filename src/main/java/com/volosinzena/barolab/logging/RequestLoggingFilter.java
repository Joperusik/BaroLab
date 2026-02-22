package com.volosinzena.barolab.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Value("${app.logging.slow-request-ms:500}")
    private long slowRequestMs;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        long startNs = System.nanoTime();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = (System.nanoTime() - startNs) / 1_000_000;
            String method = request.getMethod();
            String path = request.getRequestURI();
            int status = response.getStatus();

            if (durationMs >= slowRequestMs) {
                log.warn("Slow request {} {} status={} durationMs={}", method, path, status, durationMs);
            } else {
                log.info("Request {} {} status={} durationMs={}", method, path, status, durationMs);
            }
        }
    }
}

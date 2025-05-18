package com.example.eventManagement.domain;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    /**
     * This limits 10 requests per minute per user or IP.
     * */
    private Bucket resolveBucket(String key) {
        return cache.computeIfAbsent(key, k -> {
            Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1))); // 10 requests per minute
            return Bucket.builder().addLimit(limit).build();
        });
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String key = resolveKey(request);
        Bucket bucket = resolveBucket(key);

        if (bucket.tryConsume(1)) {
            return true;
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests");
            return false;
        }
    }

    private String resolveKey(HttpServletRequest request) {
        String user = request.getUserPrincipal() != null
                ? request.getUserPrincipal().getName()
                : request.getRemoteAddr();
        return user;
    }
}

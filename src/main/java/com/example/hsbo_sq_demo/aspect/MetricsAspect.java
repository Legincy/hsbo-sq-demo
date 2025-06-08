package com.example.hsbo_sq_demo.aspect;


import com.example.hsbo_sq_demo.service.MetricsService;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;

@Component
public class MetricsAspect {
    private final MetricsService metricsService;

    public MetricsAspect(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object measureApiCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Timer.Sample sample = metricsService.startApiCallTimer();

        try {
            Object result = joinPoint.proceed();
            metricsService.incrementApiCallCount(methodName);

            return result;
        } finally {
            metricsService.stopApiCallTimer(sample, methodName);
        }
    }
}

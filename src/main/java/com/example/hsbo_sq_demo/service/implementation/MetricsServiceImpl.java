package com.example.hsbo_sq_demo.service.implementation;

import com.example.hsbo_sq_demo.service.MetricsService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class MetricsServiceImpl implements MetricsService {
    private final MeterRegistry meterRegistry;
    private final ConcurrentHashMap<String, Counter> counters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Timer> timers = new ConcurrentHashMap<>();

    public MetricsServiceImpl(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void incrementApiCallCount(String endpoint) {
        getOrCreateCounter("api.calls.count", "endpoint", endpoint).increment();
    }

    @Override
    public Timer.Sample startApiCallTimer() {
        return Timer.start(meterRegistry);
    }

    @Override
    public void stopApiCallTimer(Timer.Sample sample, String endpoint) {
        sample.stop(getOrCreateTimer("api.calls.time", "endpoint", endpoint));
    }

    @Override
    public Counter getOrCreateCounter(String name, String tagKey, String tagValue) {
        String key = name + "." + tagKey + "." + tagValue;
        return counters.computeIfAbsent(key, k ->
                Counter.builder(name)
                        .tag(tagKey, tagValue)
                        .description("Counter for " + name)
                        .register(meterRegistry)
        );
    }

    @Override
    public Timer getOrCreateTimer(String name, String tagKey, String tagValue) {
        String key = name + "." + tagKey + "." + tagValue;
        return timers.computeIfAbsent(key, k ->
                Timer.builder(name)
                        .tag(tagKey, tagValue)
                        .description("Timer for " + name)
                        .publishPercentiles(0.5, 0.95, 0.99)
                        .register(meterRegistry)
        );
    }
}

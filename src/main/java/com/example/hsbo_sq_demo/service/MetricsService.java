package com.example.hsbo_sq_demo.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;

public interface MetricsService {
    void incrementApiCallCount(String endpoint);
    Timer.Sample startApiCallTimer();
    void stopApiCallTimer(Timer.Sample sample, String endpoint);
    Counter getOrCreateCounter(String name, String tagKey, String tagValue);
    Timer getOrCreateTimer(String name, String tagKey, String tagValue);
}

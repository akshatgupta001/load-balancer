package org.example.utility;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerHealth {
    private final String serverAddress;
    private final AtomicBoolean isHealthy;
    private final AtomicInteger failedAttempts;
    private long lastChecked;
    private static final int MAX_FAILURES = 3;

    public ServerHealth(String serverAddress) {
        this.serverAddress = serverAddress;
        this.isHealthy = new AtomicBoolean(true);
        this.failedAttempts = new AtomicInteger(0);
        this.lastChecked = System.currentTimeMillis();
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public boolean isHealthy() {
        return isHealthy.get();
    }

    public void markUnhealthy() {
        int failures = failedAttempts.incrementAndGet();
        if (failures >= MAX_FAILURES) {
            isHealthy.set(false);
        }
    }

    public void markHealthy() {
        failedAttempts.set(0);
        isHealthy.set(true);
    }

    public void updateLastChecked() {
        this.lastChecked = System.currentTimeMillis();
    }

    public long getLastChecked() {
        return lastChecked;
    }

    public int getFailedAttempts() {
        return failedAttempts.get();
    }
} 
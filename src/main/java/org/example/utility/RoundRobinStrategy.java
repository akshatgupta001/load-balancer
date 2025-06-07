package org.example.utility;

import org.example.exception.LoadBalancerException;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Round Robin load balancing strategy implementation
 */
public class RoundRobinStrategy implements LoadBalancingStrategy {
    private final List<String> servers;
    private final AtomicInteger counter;

    public RoundRobinStrategy(List<String> servers) {
        if (servers == null || servers.isEmpty()) {
            throw new IllegalArgumentException("Server list cannot be null or empty");
        }
        this.servers = servers;
        this.counter = new AtomicInteger(0);
    }

    @Override
    public String selectServer() throws LoadBalancerException {
        if (servers.isEmpty()) {
            throw new LoadBalancerException("No servers available");
        }
        return servers.get(counter.getAndIncrement() % servers.size());
    }

    @Override
    public void reportServerFailure(String server) {
        // Simple implementation - just remove the server from the list
        servers.remove(server);
    }

    @Override
    public void reportServerSuccess(String server) {
        // Simple implementation - just add the server back if it's not in the list
        if (!servers.contains(server)) {
            servers.add(server);
        }
    }

    @Override
    public int getHealthyServerCount() {
        return servers.size();
    }
} 